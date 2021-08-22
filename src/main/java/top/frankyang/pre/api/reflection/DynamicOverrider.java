package top.frankyang.pre.api.reflection;

import com.mojang.datafixers.util.Pair;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jetbrains.annotations.NotNull;
import top.frankyang.pre.api.misc.Castable;
import top.frankyang.pre.api.reflection.mapping.SymbolResolver;
import top.frankyang.pre.api.util.ArrayUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public abstract class DynamicOverrider<T> extends Enhancer implements RuntimeAccessor<T>, MethodInterceptor, Castable<T> {
    private static final SymbolResolver RESOLVER = SymbolResolver.getInstance();
    /**
     * 方法的缓存。一旦一个方法被证实可以被委托给该类，它就会被加入这个缓存。
     */
    protected final Map<MethodWrapper, Method> overrides = new ConcurrentHashMap<>();
    /**
     * 方法返回值的缓存。一旦一个方法被标注为常量方法，它的返回值就会被加入这个缓存。
     */
    protected final Map<MethodWrapper, Object> constants = new ConcurrentHashMap<>();
    /**
     * 方法的缓存。一旦一个方法被证实不可以被委托给该类，它就会被加入这个缓存。
     */
    protected final Set<MethodWrapper> noOverrides = Collections.newSetFromMap(new ConcurrentHashMap<>());
    /**
     * 当前的类。因为此类是一个不可能被实例化的抽象类，因此它总是此类的子类。
     */
    protected final Class<? extends DynamicOverrider<T>> currentClass = (Class<? extends DynamicOverrider<T>>) this.getClass();
    /**
     * 要重写方法的类。这个类对象应当是T或其子类以供重写方法。
     */
    protected final Class<? extends T> targetClass;
    /**
     * 该实例所持有的对象。
     */
    private T target;

    /**
     * 创建一个动态重写器。在使用目标对象之前，应当先调用{@link DynamicOverrider#superConstructor(Class[], Object...)}。
     *
     * @param targetClass 目标类。
     */
    public DynamicOverrider(Class<? extends T> targetClass, Class<?>... interfaces) {
        super();

        for (Class<?> $interface : interfaces) {
            if (!$interface.isInterface()) {
                throw new IllegalArgumentException("Class passed in as an interface (" + $interface.getName() + ") is not an interface.");
            }
        }

        this.targetClass = targetClass;
        setSuperclass(this.targetClass);
        setInterfaces(interfaces);
        setCallback(this);

        Set<String> excludedNames = Arrays.stream(DynamicOverrider.class.getMethods())
            .map(Method::getName)
            .collect(Collectors.toSet());
        Set<Method> methods = Stream.concat(
            Arrays.stream(targetClass.getMethods()),
            Arrays.stream(interfaces)
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
        ).collect(Collectors.toSet());
        Set<String> candidateNames = methods.stream()
            .map(RESOLVER::mappingNameOf)
            .collect(Collectors.toSet());

        ArrayList<Pair<Method, DynamicOverride>> candidates = new ArrayList<>();
        Arrays.stream(currentClass.getMethods())
            .filter(DynamicOverrider::hasAnnotation)
            .map(m -> new Pair<>(m, getAnnotation(m)))
            .filter(p -> !excludedNames.contains(nameOrAliasOf(p)))
            .filter(p -> candidateNames.contains(nameOrAliasOf(p)))
            .forEach(candidates::add);

        for (Method superMethod : methods) {
            String name = RESOLVER.mappingNameOf(superMethod);
            Pair<Method, DynamicOverride> override = candidates.stream()
                .filter(p -> nameOrAliasOf(p).equals(name) && isCompatible(superMethod, p.getFirst()))
                .findFirst()
                .orElse(null);
            if (override != null) {
                Method m = override.getFirst();
                m.setAccessible(true);
                if (override.getSecond().constant()) {
                    Object o = invoke(m);
                    constants.put(new MethodWrapper(superMethod), o);
                } else {
                    overrides.put(new MethodWrapper(superMethod), m);
                }
                candidates.remove(override);
            } else {
                noOverrides.add(new MethodWrapper(superMethod));
            }
        }
        candidates.removeIf(m -> !m.getSecond().required());
        if (!candidates.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            candidates.forEach(
                p -> builder.append("\tDoes not match: ").append(p.getFirst()).append("\n")
            );
            throw new RuntimeException("Not all methods matched a super method: \n" + builder);
        }
    }

    private static boolean hasAnnotation(Method method) {
        return method.getAnnotationsByType(DynamicOverride.class).length > 0;
    }

    private static DynamicOverride getAnnotation(Method method) {
        return method.getAnnotationsByType(DynamicOverride.class)[0];
    }

    private static String nameOrAliasOf(Pair<Method, DynamicOverride> p) {
        if (p.getSecond().value().isEmpty())
            return p.getFirst().getName();
        return p.getSecond().value();
    }

    private static boolean isCompatible(Method source, Method override) {
        Class<?>[] srcTypes = ArrayUtils.mergeArrays(
            Class.class, source.getParameterTypes(), MethodContainer.class
        );
        Class<?>[] dstTypes = override.getParameterTypes();
        if (srcTypes.length != dstTypes.length) {
            return false;
        }
        for (int i = 0; i < srcTypes.length; i++) {
            if (!dstTypes[i].isAssignableFrom(srcTypes[i])) return false;
        }
        return source.getReturnType().isAssignableFrom(override.getReturnType());
    }

    private Object invoke(Method method) {
        try {
            return method.invoke(this, new Object[method.getParameterTypes().length]);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        MethodWrapper finder = new MethodWrapper(method);
        if (noOverrides.contains(finder)) {
            return proxy.invokeSuper(obj, args);
        }
        if (constants.containsKey(finder)) {
            return constants.get(finder);
        }
        MethodContainer container = new MethodContainer() {
            @Override
            public Object invoke0(Object[] args0) {
                try {
                    return proxy.invokeSuper(obj, args0);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }

            @Override
            public Class<?>[] getParamTypes() {
                return method.getParameterTypes();
            }

            @Override
            public Class<?> getReturnType() {
                return method.getReturnType();
            }
        };
        if (overrides.containsKey(finder)) {
            Method override = overrides.get(finder);
            return override.invoke(this, ArrayUtils.mergeArrays(Object.class, args, container));
        }
        return proxy.invokeSuper(obj, args);
    }

    @Override
    public Class<? extends T> getClazz() {
        return targetClass;
    }

    @Override
    public @NotNull T getTarget() {
        if (target == null)
            throw new NullPointerException("Target not constructed yet.");
        return target;
    }

    /**
     * 调用目标类的构造方法，返回一个实例。
     *
     * @param classes 目标类构造器的参数类型。
     * @param args    目标类构造器的参数。
     * @return 构造的实例。
     */
    public T superConstructor(Class<?>[] classes, Object... args) {
        if (target != null)
            throw new NullPointerException("Target already constructed.");
        return target = (T) create(classes, args);
    }

    private static class MethodWrapper {
        public final Method method;

        private MethodWrapper(Method method) {
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodWrapper that = (MethodWrapper) o;
            return this.method.getName().equals(that.method.getName()) &&
                Arrays.equals(
                    this.method.getParameterTypes(),
                    that.method.getParameterTypes());
        }

        @Override
        public int hashCode() {
            return method.getName().hashCode() ^
                Arrays.hashCode(method.getParameterTypes());
        }
    }
}
