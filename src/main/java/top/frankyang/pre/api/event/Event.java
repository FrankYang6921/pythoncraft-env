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
     * 抑制一个事件的默认动作。
     *
     * @return 该操作是否成功完成。
     */
    default boolean suppressDefaultAction() {
        return false;
    }

    /**
     * 获取该事件的默认动作是否被抑制。
     *
     * @return 默认动作是否被抑制。
     */
    default boolean defaultActionSuppressed() {
        return true;
    }

    /**
     * 内部使用的方法，供事件源回调。
     */
    default void beforeListeners() {
    }

    /**
     * 内部使用的方法，供事件源回调。
     */
    default void afterListeners() {
    }
}
