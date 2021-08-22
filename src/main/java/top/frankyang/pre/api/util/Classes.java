package top.frankyang.pre.api.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public final class Classes {
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

    private Classes() {
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

    public static Set<Field> getAllFields(Class<?> c) {
        Set<Field> set = new HashSet<>();
        Collections.addAll(set, c.getDeclaredFields());
        Collections.addAll(set, c.getFields());
        return Collections.unmodifiableSet(set);
    }

    public static List<Field> getInstanceFields(Class<?> c) {
        return Classes.getAllFields(c)
            .stream()
            .peek(f -> f.setAccessible(true))
            .filter(f -> !Modifier.isStatic(f.getModifiers()))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public static List<Field> getStaticFields(Class<?> c) {
        return Classes.getAllFields(c)
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

    public static Object assignTo(Object src, Object dst) {
        if (!src.getClass().isAssignableFrom(dst.getClass())) {
            throw new IllegalArgumentException(src + ", " + dst);
        }
        for (Field field : getInstanceFields(src.getClass())) {
            try {
                field.set(dst, field.get(src));
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
}
