package top.frankyang.pre.api.misc;

/**
 * 表明一个对象是可复制的。
 *
 * @param <T> 该对象的类型。
 */
public interface Copyable<T> {
    <U extends T> U copy();

    default <U extends T> U deepCopy() {
        return copy();
    }
}
