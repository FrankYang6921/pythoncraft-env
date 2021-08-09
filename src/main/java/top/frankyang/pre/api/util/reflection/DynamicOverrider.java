package top.frankyang.pre.api.util.reflection;

import com.mojang.datafixers.util.Pair;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jetbrains.annotations.NotNull;
import top.frankyang.pre.api.misc.Castable;
import top.frankyang.pre.loader.exceptions.ImpossibleException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class DynamicOverrider<T> extends Enhancer implements RuntimeAccessor<T>, MethodInterceptor, Castable<T> {
    private static final SymbolResolver RESOLVER = SymbolResolver.getInstance();
    /**
     * 方法的缓存。一旦一个方法被证实可以被委托给该类，它就会被加入这个缓存。
     */
    protected final Map<MethodWrapper, Method> overrides = new HashMap<>();
    /**
     * 方法返回值的缓存。一旦一个方法被标注为常量方法，它的返回值就会被加入这个缓存。
     */
    protected final Map<MethodWrapper, Object> constants = new HashMap<>();
    /**
     * 方法的缓存。一旦一个方法被证实不可以被委托给该类，它就会被加入这个缓存。
     */
    protected final HashSet<MethodWrapper> noOverrides = new HashSet<>();
    /**
     * 当前的类。因为此类是一个不可能被实例化的抽象类，因此它总是此类的子类。
     */
    protected final Class<? extends DynamicOverrider<T>> currentClass =
        (Class<? extends DynamicOverrider<T>>) this.getClass();
    /**
     * 要重写方法的类。这个类对象应当是T或其子类以供重写方法。
     */
    protected final Class<? extends T> targetClass;
    /**
     * 该实例所持有的对象。
     */
    private T target;

    public DynamicOverrider(Class<? extends T> targetClass) {
        super();
        this.targetClass = targetClass;
        setSuperclass(this.targetClass);
        setCallback(this);

        Set<String> excludedNames = Arrays.stream(DynamicOverrider.class.getMethods())
            .map(Method::getName)
            .collect(Collectors.toSet());
        Set<String> candidateNames = Arrays.stream(targetClass.getMethods())
            .map(RESOLVER::mappingNameOf)
            .collect(Collectors.toSet());

        ArrayList<Pair<Method, DynamicOverride>> candidates = new ArrayList<>();
        Arrays.stream(currentClass.getMethods())
            .filter(DynamicOverrider::hasAnnotation)
            .map(m -> new Pair<>(m, getAnnotation(m)))
            .filter(p -> !excludedNames.contains(nameOf(p)))
            .filter(p -> candidateNames.contains(nameOf(p)))
            .forEach(candidates::add);

        for (Method superMethod : targetClass.getMethods()) {
            String name = RESOLVER.mappingNameOf(superMethod);
            Pair<Method, DynamicOverride> override = candidates.stream()
                .filter(p -> nameOf(p).equals(name) && isCompatible(superMethod, p.getFirst()))
                .findFirst()
                .orElse(null);
            if (override != null) {
                Method m = override.getFirst();
                m.setAccessible(true);
                if (override.getSecond().constant()) {
                    Object o = invokeAsAllNull(m);
                    constants.put(new MethodWrapper(superMethod), o);
                } else {
                    overrides.put(new MethodWrapper(superMethod), m);
                }
                candidates.remove(override);
            } else {
                noOverrides.add(new MethodWrapper(superMethod));
            }
        }
        if (!candidates.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            candidates.forEach(
                p -> builder.append("DOES NOT MATCH: ").append(p.getFirst()).append("\n")
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

    private static String nameOf(Pair<Method, DynamicOverride> p) {
        if (p.getSecond().value().isEmpty())
            return p.getFirst().getName();
        return p.getSecond().value();
    }

    private static Class<?>[] ofClasses(Object[] objects) {
        Class<?>[] classes = new Class[objects.length];
        for (int i = 0; i < classes.length; i++)
            classes[i] = objects[i].getClass();
        return classes;
    }

    private static <U> U[] mergeArrays(Class<U> clazz, U[] array, U... more) {
        U[] newArray = (U[]) Array.newInstance(clazz, array.length + more.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        System.arraycopy(more, 0, newArray, array.length, more.length);
        return newArray;
    }

    private static boolean isCompatible(Method source, Method override) {
        Class<?>[] srcTypes = mergeArrays(
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

    public void createTarget(Class<?>[] classes, Object... args) {
        if (target != null)
            throw new IllegalStateException("Target already created.");
        target = (T) create(classes, args);
    }

    private Object invokeAsAllNull(Method method) {
        try {
            return method.invoke(this, new Object[method.getParameterTypes().length]);
        } catch (ReflectiveOperationException e) {
            throw new ImpossibleException(e);
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
            return override.invoke(
                this, mergeArrays(Object.class, args, container)
            );
        }
        return proxy.invokeSuper(obj, args);
    }

    @Override
    public final Class<? extends T> getTargetClass() {
        return targetClass;
    }

    @Override
    public final @NotNull T getTarget() {
        if (target == null)
            throw new NullPointerException("Target not created yet.");
        return target;
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
            return method.getName().hashCode() ^ Arrays.hashCode(method.getParameterTypes());
        }
    }
}
