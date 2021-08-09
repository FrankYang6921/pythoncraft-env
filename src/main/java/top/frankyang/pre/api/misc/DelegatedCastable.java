package top.frankyang.pre.api.misc;

/**
 * 声明一个可转型的类，内含一个要转型到的对象。
 *
 * @param <T> 可转型的类。
 */
public abstract class DelegatedCastable<T> implements Castable<T> {
    protected T delegate;

    protected DelegatedCastable(T delegate) {
        this.delegate = delegate;
    }

    @Override
    public T cast() {
        return delegate;
    }
}
