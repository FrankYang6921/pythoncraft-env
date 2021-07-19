package top.frankyang.pre.api.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 魔法常量解析器，可以通过反射获取一个类中指定类型的魔法常量值。
 *
 * @param <T> 所要解析的魔法常量的类型。
 */
public class MagicConstants<T> {
    private final Class<?> clazz;
    private final Map<String, T> cache = new HashMap<>();

    public MagicConstants(Class<?> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    private T get0(String k) {
        try {
            Field field = clazz.getDeclaredField(k);
            field.setAccessible(true);
            Object t = field.get(null);
            if (clazz.isInstance(t))
                return (T) t;
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 按照静态字段名获取一个魔法常量。
     *
     * @param k 静态字段名。
     * @return 获取到的魔法常量。
     * @throws NullPointerException     如果静态字段名是<code>null</code>。
     * @throws IllegalArgumentException 如果找不到对应的静态字段。
     */
    public T get(String k) {
        Objects.requireNonNull(k);
        T t = cache.computeIfAbsent(k, this::get0);
        if (t == null)
            throw new IllegalArgumentException(k);
        return t;
    }

    /**
     * 按照静态字段名获取一个魔法常量。如果找不到对应的静态字段，或者静态字段名是<code>null</code>，返回默认值。
     *
     * @param k     静态字段名。
     * @param value 默认值。
     * @return 获取到的魔法常量。
     * @throws NullPointerException     如果静态字段名是<code>null</code>。
     * @throws IllegalArgumentException 如果找不到对应的静态字段。
     */
    public T getOrDefault(String k, T value) {
        if (k == null)
            return value;
        T t = cache.computeIfAbsent(k, this::get0);
        if (t == null)
            return value;
        return t;
    }
}
