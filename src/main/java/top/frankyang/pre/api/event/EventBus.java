package top.frankyang.pre.api.event;

/**
 * 事件源组。事件源组持有多个{@link EventSource 事件源}。
 *
 * @param <T> 要监听和触发的事件类型。
 */
public interface EventBus<T extends Event> {
    /**
     * 创建一种事件源。
     *
     * @param type 该事件源的名称。
     * @return 得到的事件源。
     */
    EventSource<T> newSource(String type);

    /**
     * 获取一种事件源。
     *
     * @param type 该事件源的名称。
     * @return 得到的事件源。
     */
    EventSource<T> getSource(String type);

    /**
     * 触发一种事件源。
     *
     * @param type  该事件源的名称。
     * @param event 用于触发事件源的事件。
     * @return 成功触发的侦听器的数量。
     * @see EventSource#trigger(Event)
     */
    int trigger(String type, T event);

    /**
     * 订阅一种事件源。
     *
     * @param listener 用于订阅事件的事件侦听器。
     * @see EventSource#subscribe(EventListener)
     */
    EventBus<T> subscribe(String type, EventListener<? super T> listener);

    /**
     * 取消订阅一种事件源。
     *
     * @param listener 要取消订阅的事件侦听器。
     * @see EventSource#unsubscribe(EventListener)
     */
    EventBus<T> unsubscribe(String type, EventListener<? super T> listener);
}
