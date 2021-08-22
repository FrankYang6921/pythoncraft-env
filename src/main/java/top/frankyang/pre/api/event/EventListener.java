package top.frankyang.pre.api.event;

/**
 * 事件监听器。
 *
 * @param <T> 要侦听的事件类型。
 */
@FunctionalInterface
public interface EventListener<T extends Event> {
    /**
     * 侦听事件的回调。
     *
     * @param t 事件。
     * @throws Throwable 如果事件处理过程中出现了任何错误。
     */
    void trigger(T t) throws Throwable;
}
