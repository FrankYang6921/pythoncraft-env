package top.frankyang.pre.api.event;

/**
 * 具有返回值的事件。它可以设定和获取返回值。
 *
 * @param <T> 返回值的类型。
 */
public interface ReturnableEvent<T> extends Event {
    T getReturnValue();

    void setReturnValue(T t);
}
