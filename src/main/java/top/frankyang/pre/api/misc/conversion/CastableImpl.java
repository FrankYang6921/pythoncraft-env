package top.frankyang.pre.api.misc.conversion;

import java.util.Objects;

/**
 * 声明一个可转型的类，内含一个要转型到的对象。
 *
 * @param <T> 可转型的类。
 */
public abstract class CastableImpl<T> implements Castable<T> {
    protected T casted;

    protected CastableImpl() {
        this.casted = null;
    }

    protected CastableImpl(T casted) {
        this.casted = casted;
    }

    @Override
    public T cast() {
        return Objects.requireNonNull(casted);
    }
}
