package top.frankyang.pre.api.reflection.mapping;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内部工具类，用于找到一个方法或字段的最初声明。因为这些操作比较耗时，所以结果是缓存的。
 */
final class DeclarationFinder {
    private static final Map<TripleItems, Class<?>> fieldCache = new ConcurrentHashMap<>();
    private static final Map<TripleItems, Class<?>> methodCache = new ConcurrentHashMap<>();

    private DeclarationFinder() {
    }

    private static boolean hasField(Class<?> clazz, String name, Class<?>[] types) {
        try {
            clazz.getField(name);
        } catch (ReflectiveOperationException e) {
            try {
                clazz.getDeclaredField(name);
            } catch (ReflectiveOperationException e1) {
                return false;
            }
            return true;
        }
        return true;
    }

    private static boolean hasMethod(Class<?> clazz, String name, Class<?>[] types) {
        try {
            clazz.getMethod(name, types);
        } catch (ReflectiveOperationException e) {
            try {
                clazz.getDeclaredMethod(name, types);
            } catch (ReflectiveOperationException e1) {
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * @param clazz 字段所属的类对象。
     * @param name  字段的运行时名称。
     * @param types 未被使用。可传入任意值。
     * @return 最初声明此字段的类对象。
     */
    static Class<?> getOriginalFieldDeclaration(Class<?> clazz, String name, Class<?>[] types) {
        TripleItems items = new TripleItems(clazz, name, types);
        return fieldCache.computeIfAbsent(
            items, i -> getOriginalDeclaration(clazz, name, types, DeclarationFinder::hasField)
        );
    }

    /**
     * @param clazz 方法所属的类对象。
     * @param name  方法的运行时名称。
     * @param types 方法的形参类对象。
     * @return 最初声明此方法的类对象。
     */
    static Class<?> getOriginalMethodDeclaration(Class<?> clazz, String name, Class<?>[] types) {
        TripleItems items = new TripleItems(clazz, name, types);
        return methodCache.computeIfAbsent(
            items, i -> getOriginalDeclaration(clazz, name, types, DeclarationFinder::hasMethod)
        );
    }

    private static Class<?> getOriginalDeclaration(Class<?> clazz, String name, Class<?>[] types, Condition condition) {
        Class<?> result = getOriginalDeclaration0(clazz, name, types, condition);
        if (result == null)
            return clazz;
        return result;
    }

    private static Class<?> getOriginalDeclaration0(Class<?> clazz, String name, Class<?>[] types, Condition condition) {
        // Without specified target
        if (!condition.test(clazz, name, types)) {
            return null;
        }
        // Impossible to search further, return.
        Class<?>[] interfaces = clazz.getInterfaces();
        Class<?> superclass = clazz.getSuperclass();
        if (interfaces.length == 0 && superclass == null) {
            return clazz;
        }
        // Try to find declaration in interfaces.
        for (Class<?> $interface : interfaces) {
            Class<?> result = getOriginalDeclaration0($interface, name, types, condition);
            if (result != null) {
                return result;
            }
        }
        // Try to find declaration in superclass.
        Class<?> result = getOriginalDeclaration0(superclass, name, types, condition);
        if (result != null) {
            return result;
        }
        return clazz;
    }

    @FunctionalInterface
    private interface Condition {
        boolean test(Class<?> clazz, String name, Class<?>[] types);
    }

    private static class TripleItems {
        private final Object a, b, c;

        private TripleItems(Object a, Object b, Object c) {
            this.a = Objects.requireNonNull(a);
            this.b = Objects.requireNonNull(b);
            this.c = Objects.requireNonNull(c);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TripleItems that = (TripleItems) o;
            return a.equals(that.a) && b.equals(that.b) && c.equals(that.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }
    }
}
