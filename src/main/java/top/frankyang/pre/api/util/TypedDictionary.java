package top.frankyang.pre.api.util;

import org.python.core.PyDictionary;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
     * @param value 要返回的默认值。
     * @param <T>   要获取的值的类。
     * @return 获取到的值。
     */
    public <T> T getOrDefault(String key, Class<T> clazz, T value) {
        Objects.requireNonNull(key);
        T t = get(key, clazz);
        if (t == null)
            return Objects.requireNonNull(value);
        return t;
    }

    /**
     * 从字典中按指定类型获取一个值。如果找不到指定的键值对，或者找到的值不是指定的类型，计算默认值。
     *
     * @param key      值所对应的键。
     * @param clazz    要获取的值的类实例。
     * @param function 计算默认值的方法。
     * @param <T>      要获取的值的类。
     * @return 获取到的值。
     */
    public <T> T getOrCompute(String key, Class<T> clazz, Supplier<T> function) {
        Objects.requireNonNull(key);
        T t = get(key, clazz);
        if (t == null)
            return Objects.requireNonNull(function.get());
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
     * 从字典中按指定类型获取一个布尔值，如果是真则执行函数。如果找不到指定的键值对，或者找到的值不是布尔值，不做任何事。
     *
     * @param key      值所对应的键。
     * @param runnable 要执行的函数。
     * @param <T>      要获取的值的类。
     */
    public <T> void ifTrue(String key, Runnable runnable) {
        ifPresent(key, Boolean.class, b -> {
            if (b) runnable.run();
        });
    }

    /**
     * 从字典中按指定类型获取一个指定类型的值并执行函数。如果找不到指定的键值对，或者找到的值不是指定类型，不做任何事。
     *
     * @param key      值所对应的键。
     * @param clazz    要获取的值的类实例。
     * @param consumer 要执行的函数。
     * @param <T>      要获取的值的类。
     */
    public <T> void ifPresent(String key, Class<T> clazz, Consumer<T> consumer) {
        computeIfPresent(key, clazz, t -> {
            consumer.accept(t);
            return null;
        });
    }

    /**
     * 从字典中按指定类型获取一个指定类型的值并执行函数且返回它的返回值。如果找不到指定的键值对，或者找到的值不是指定类型，返回<code>null</code>。
     *
     * @param key      值所对应的键。
     * @param clazz    要获取的值的类实例。
     * @param function 要执行的函数。
     * @param <T>      要获取的值的类。
     * @param <U>      要返回的值的类。
     */
    public <T, U> U computeIfPresent(String key, Class<T> clazz, Function<T, U> function) {
        return computeIfPresent(key, clazz, function, null);
    }

    /**
     * 从字典中按指定类型获取一个指定类型的值并执行函数且返回它的返回值。如果找不到指定的键值对，或者找到的值不是指定类型，返回默认值。
     *
     * @param key      值所对应的键。
     * @param function 要执行的函数。
     * @param value    要返回的默认值。
     * @param <T>      要获取的值的类。
     * @param <U>      要返回的值的类。
     */
    public <T, U> U computeIfPresent(String key, Class<T> clazz, Function<T, U> function, U value) {
        T t = get(key, clazz);
        if (t != null) {
            return function.apply(t);
        }
        return value;
    }
}
