package top.frankyang.pre.api.util;

import org.python.core.PyDictionary;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 带有类型的Python字典。
 */
public class TypedDictionary {
    private final PyDictionary dictionary;

    public TypedDictionary(PyDictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * 从字典中按指定类型获取一个值。
     *
     * @param key   值所对应的键。
     * @param clazz 要获取的值的类实例。
     * @param <T>   要获取的值的类。
     * @return 获取到的值。
     * @throws IllegalArgumentException 如果找不到指定的键值对，或者找到的值不是指定的类型。
     */
    public <T> T getRequired(String key, Class<T> clazz) {
        Objects.requireNonNull(key);
        T t = get(key, clazz);
        if (t == null)
            throw new IllegalArgumentException('"' + key + "\": " + clazz.getSimpleName());
        return t;
    }

    /**
     * 从字典中按指定类型获取一个值。如果找不到指定的键值对，或者找到的值不是指定的类型，返回默认值。
     *
     * @param key   值所对应的键。
     * @param clazz 要获取的值的类实例。
     * @param <T>   要获取的值的类。
     * @return 获取到的值。
     */
    public <T> T getOrDefault(String key, Class<T> clazz, T value) {
        Objects.requireNonNull(key);
        T t = get(key, clazz);
        if (t == null)
            return value;
        return t;
    }

    /**
     * 从字典中按指定类型获取一个值。如果找不到指定的键值对，或者找到的值不是指定的类型，返回<code>null</code>。
     *
     * @param key   值所对应的键。
     * @param clazz 要获取的值的类实例。
     * @param <T>   要获取的值的类。
     * @return 获取到的值。
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Objects.requireNonNull(key);
        Object t;
        if (!dictionary.containsKey(key) || !clazz.isInstance(t = dictionary.get(key)))
            return null;
        return (T) t;
    }

    /**
     * 从字典中按指定类型获取一个值并执行操作。如果找不到指定的键值对，或者找到的值不是指定的类型，不做任何事。
     *
     * @param key      值所对应的键。
     * @param clazz    要获取的值的类实例。
     * @param consumer 要执行的操作。
     * @param <T>      要获取的值的类。
     */
    public <T> void ifPresent(String key, Class<T> clazz, Consumer<T> consumer) {
        T t = get(key, clazz);
        if (t != null) {
            consumer.accept(t);
        }
    }
}
