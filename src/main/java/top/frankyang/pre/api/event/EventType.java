package top.frankyang.pre.api.event;

/**
 * 事件类型枚举。包含了所有可能的事件类型。
 * @see Event#getType()
 */
public enum EventType {
    DUMMY,
    BLOCK_BREAK_START,
    BLOCK_BREAK_FINISH,
    BLOCK_BREAK_AFTER
}
