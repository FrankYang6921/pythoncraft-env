package top.frankyang.pre.api.event;

/**
 * 事件源组。事件源组持有多个{@link EventSource 事件源}。
 *
 * @param <T> 要监听和触发的事件类型。
 */
public interface EventSources<T extends Event> {
    /**
     * 创建一种事件源。
     *
     * @param type 该事件源的名称。
     */
    void create(String type);

    /**
     * 触发一种事件源。
     *
     * @param type 该事件源的名称。
     * @param event 用于触发事件源的事件。
     * @see EventSource#trigger(Event)
     */
    boolean trigger(String type, T event);

    /**
     * 订阅一种事件源。
     *
     * @param listener 用于订阅事件的事件侦听器。
     * @see EventSource#subscribe(EventListener)
     */
    void subscribe(String type, EventListener<? super T> listener);

    /**
     * 取消订阅一种事件源。
     *
     * @param listener 要取消订阅的事件侦听器。
     * @see EventSource#unsubscribe(EventListener)
     */
    void unsubscribe(String type, EventListener<? super T> listener);
}
