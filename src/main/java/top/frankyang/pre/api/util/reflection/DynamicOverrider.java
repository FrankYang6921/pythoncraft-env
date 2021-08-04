package top.frankyang.pre.api.util.reflection;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.frankyang.pre.loader.exceptions.ImpossibleException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class DynamicOverrider<T>
    extends Enhancer
    implements
    RuntimeAccessor<T>,
    MethodInterceptor {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SymbolResolver RESOLVER = SymbolResolver.getInstance();
    private static final Map<Method, String> aliasCache = new HashMap<>();
    private static final Map<Method, Boolean> constantCache = new HashMap<>();
    /**
     * 方法的缓存。一旦一个方法被证实可以被委托给该类，它就会被加入这个缓存。
     */
    protected final Map<Method, Method> trueCache = new HashMap<>();
    /**
     * 方法返回值的缓存。一旦一个方法被标注为常量方法，它的返回值就会被加入这个缓存。
     */
    protected final Map<Method, Object> resultCache = new HashMap<>();
    /**
     * 方法的缓存。一旦一个方法被证实不可以被委托给该类，它就会被加入这个缓存。
     */
    protected final HashSet<Method> falseCache = new HashSet<>();
    /**
     * 潜在的重写方法的列表。该字段用于加快查找一个原方法重写候选时的速度。
     */
    protected final HashSet<Method> candidates = new HashSet<>();
    /**
     * 当前的类。因为此类是一个不可能被实例化的抽象类，因此它总是此类的子类。
     */
    protected final Class<? extends DynamicOverrider<T>> currentClass =
        (Class<? extends DynamicOverrider<T>>) this.getClass();
    /**
     * 要重写方法的类。这个类对象应当是<code>T.class</code>或其子类以供重写方法。
     */
    protected final Class<? extends T> targetClass;
    /**
     * 该实例所持有的对象。
     */
    protected final T target;

    public DynamicOverrider(Class<? extends T> targetClass, Class<?>[] classes, Object... args) {
        super();
        this.targetClass = targetClass;
        setSuperclass(this.targetClass);
        setCallback(this);

        // Creates the target object.
        target = (T) create(classes, args);

        Set<String> excludedNames = Arrays.stream(DynamicOverrider.class.getMethods())
            .map(Method::getName)
            .collect(Collectors.toSet());
        Set<String> candidateNames = Arrays.stream(target.getClass().getMethods())
            .map(RESOLVER::mappingNameOf)
            .collect(Collectors.toSet());
        Arrays.stream(currentClass.getMethods())
            .filter(o -> !excludedNames.contains(aliasOf(o)))
            .filter(o -> candidateNames.contains(aliasOf(o)))
            .forEach(candidates::add);

        LOGGER.info("Dynamically overridden class: `" + RESOLVER.classNameToMapping(targetClass.getName()) + "`.");
    }

    private static Class<?>[] ofClasses(Object[] objects) {
        Class<?>[] classes = new Class[objects.length];
        for (int i = 0; i < classes.length; i++)
            classes[i] = objects[i].getClass();
        return classes;
    }

    private static Object[] mergeArrays(Object[] array, Object... more) {
        Object[] newArray = new Object[array.length + more.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        System.arraycopy(more, 0, newArray, array.length, more.length);
        return newArray;
    }

    private static boolean isCompatible(Object[] objects, Class<?>[] types) {
        if (objects.length != types.length) return false;

        for (int i = 0; i < objects.length; i++) {
            if (!types[i].isInstance(objects[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCompatible(Object[] objects, Method method) {
        return isCompatible(objects, method.getParameterTypes());
    }

    private static String aliasOf(Method method) {
        return aliasCache.computeIfAbsent(method, m -> {
            DynamicOverride[] overrideInfos = method.getAnnotationsByType(DynamicOverride.class);
            // A illegal identifier. To ensure methods without this annotation is ignored.
            if (overrideInfos.length == 0)
                return "for";
            if (overrideInfos.length > 1)
                throw new ImpossibleException("Multiple @DynamicOverride annotations?");

            DynamicOverride overrideInfo = overrideInfos[0];
            if (overrideInfo.value().isEmpty())
                return method.getName();
            return overrideInfo.value();
        });
    }

    private static boolean isConstant(Method method) {
        return constantCache.computeIfAbsent(method, m -> {
            DynamicOverride[] overrideInfos = method.getAnnotationsByType(DynamicOverride.class);
            if (overrideInfos.length == 0)
                return false;
            if (overrideInfos.length > 1)
                throw new ImpossibleException("Multiple @DynamicOverride annotations?");

            DynamicOverride overrideInfo = overrideInfos[0];
            return overrideInfo.constant();
        });
    }

    @Override
    public final Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // Cached to be not overridden
        if (falseCache.contains(method)) {
            return proxy.invokeSuper(obj, args);
        }
        if (resultCache.containsKey(method)) {
            return resultCache.get(method);
        }

        Wrapper wrapper = args0 -> {
            try {
                return proxy.invokeSuper(obj, args0);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
        Object[] realArgs = mergeArrays(args, wrapper);

        // Cached to be overridden
        if (trueCache.containsKey(method)) {
            Method target = trueCache.get(method);
            return target.invoke(this, realArgs);
        }

        String realName = RESOLVER.mappingNameOf(method);

        // Find in candidate methods
        Optional<Method> first = candidates.stream().filter(method0 ->
            aliasOf(method0).equals(realName) && isCompatible(realArgs, method0) &&
                method.getReturnType().isAssignableFrom(method0.getReturnType())
        ).findFirst();

        // No override found for this method.
        if (!first.isPresent()) {
            falseCache.add(method);
            return proxy.invokeSuper(obj, args);
        }

        // An override found for this method.
        Method method0 = first.get();
        trueCache.put(method, method0);
        candidates.remove(method0);
        aliasCache.remove(method0);
        constantCache.remove(method0);

        Object result = method0.invoke(this, realArgs);
        if (isConstant(method0)) {
            return resultCache.putIfAbsent(method, result);
        }
        return result;
    }

    @Override
    public final Class<? extends T> getTargetClass() {
        return targetClass;
    }

    @Override
    public final T getTarget() {
        return target;
    }
}
