package top.frankyang.pre.api.event;

/**
 * 一个带有默认动作的事件。
 *
 * @param <T> 默认动作的类型。
 */
public interface DefaultActionEvent<T> extends Event {
    /**
     * 获取该事件的默认动作。
     *
     * @return 该事件的默认动作。
     */
    T getDefaultAction();
}
