package top.frankyang.pre.api.util;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 一个类，可以在反射中干各种各样强制性的事情。
 */
public final class ReflectUtils {
    private static final Unsafe theUnsafe;
    private static final Set<Class<?>> primitiveWrappers =
        Collections.unmodifiableSet(new HashSet<Class<?>>() {{
            add(Boolean.class);
            add(Byte.class);
            add(Character.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Void.class);
        }});

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            theUnsafe = (Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ReflectUtils() {
    }

    public static Unsafe getTheUnsafe() {
        return theUnsafe;
    }

    public static <T> T forceNew(Class<T> c) {
        try {
            //noinspection unchecked
            return (T) theUnsafe.allocateInstance(c);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void forceThrow(Throwable throwable) {
        theUnsafe.throwException(throwable);
    }

    public static void tryThrow(LooseRunnable runnable) {
        runnable.run();
    }

    public static <T> T tryThrow(LooseSupplier<T> supplier) {
        return supplier.get();
    }

    public static Set<Field> getAllFields(Class<?> c) {
        Set<Field> set = new HashSet<>();
        Collections.addAll(set, c.getDeclaredFields());
        Collections.addAll(set, c.getFields());
        if (c.getInterfaces().length != 0) {
            for (Class<?> $interface : c.getInterfaces()) {
                set.addAll(getAllFields($interface));
            }
        }
        Class<?> superClass = c.getSuperclass();
        if (superClass != null) {
            set.addAll(getAllFields(superClass));
        }
        return Collections.unmodifiableSet(set);
    }

    public static List<Field> getInstanceFields(Class<?> c) {
        return ReflectUtils.getAllFields(c)
            .stream()
            .peek(f -> f.setAccessible(true))
            .filter(f -> !Modifier.isStatic(f.getModifiers()))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public static List<Field> getStaticFields(Class<?> c) {
        return ReflectUtils.getAllFields(c)
            .stream()
            .peek(f -> f.setAccessible(true))
            .filter(f -> Modifier.isStatic(f.getModifiers()))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public static boolean isPrimitiveOnly(Class<?> c) {
        return getInstanceFields(c).stream().allMatch(f -> f.getType().isPrimitive());
    }

    public static boolean isPrimitiveWrapper(Class<?> c) {
        return primitiveWrappers.contains(c);
    }

    public static boolean isPrimitiveArray(Class<?> c) {
        return c.isArray() && c.getComponentType().isPrimitive();
    }

    public static boolean isReferenceArray(Class<?> c) {
        return c.isArray() && !c.getComponentType().isPrimitive();
    }

    public static <T, U extends T> U assignTo(T src, U dst) {
        for (Field field : getInstanceFields(src.getClass())) {
            try {
                field.set(dst, field.get(src));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return dst;
    }

    public static <T, U extends T> U deepAssignTo(T src, U dst) {
        for (Field field : getInstanceFields(src.getClass())) {
            try {
                field.set(dst, CopyUtils.deepCopy(field.get(src)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return dst;
    }


    public static Set<Class<?>> getSuperClasses(Class<?> c) {
        return getSuperClasses(c, new HashSet<>());
    }

    private static Set<Class<?>> getSuperClasses(Class<?> c, Set<Class<?>> ret) {
        Class<?> superclass = c.getSuperclass();
        ret.addAll(Arrays.asList(c.getInterfaces()));
        if (superclass == null) {
            return ret;
        }
        ret.add(superclass);
        return getSuperClasses(superclass, ret);
    }

    public static Object invokeStatic(String methodName) {
        return tryThrow(() -> {
            Method method = MethodHandles.lookup().lookupClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(null);
        });
    }

    public static Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (ReflectiveOperationException e) {
            // Search in parents
            List<Field> results = new ArrayList<>();
            for (Class<?> cls : clazz.getInterfaces()) {
                results.add(findField(cls, name));
            }
            Class<?> cls = clazz.getSuperclass();
            if (cls != null)
                results.add(findField(cls, name));
            return results.stream().filter(Objects::nonNull).findFirst().orElse(null);
        }
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(name, paramTypes);
        } catch (ReflectiveOperationException e) {
            // Search in parents
            List<Method> results = new ArrayList<>();
            for (Class<?> cls : clazz.getInterfaces()) {
                results.add(findMethod(cls, name));
            }
            Class<?> cls = clazz.getSuperclass();
            if (cls != null)
                results.add(findMethod(cls, name));
            return results.stream().filter(Objects::nonNull).findFirst().orElse(null);
        }
    }
}
