package top.frankyang.pre.api.event;

/**
 * 一个能够在回调中被使用的简单事件。
 */
public interface Event {
    /**
     * 获取一个事件的类型（应当是{@link EventType}之一）
     *
     * @return 该事件的类型。
     */
    EventType getType();

    /**
     * 内部使用的方法，供事件源回调。
     */
    default void beforeListeners(EventSource<?> source) {
    }

    /**
     * 内部使用的方法，供事件源回调。
     */
    default void afterListeners(EventSource<?> source) {
    }
}
