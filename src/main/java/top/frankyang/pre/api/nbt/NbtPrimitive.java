package top.frankyang.pre.api.nbt;

/**
 * NBT基础类型。该类型表示一个简单的持有一个值（通常是不可变值）的NBT对象，该对象默认是只读的，如果覆写了{@link NbtPrimitive#set(Object)}方法，则是可变的。
 *
 * @param <T> 该NBT对象表示的类型。
 */
public interface NbtPrimitive<T> {
    T get();

    default void set(T u) {
        throw new UnsupportedOperationException();
    }
}
