package top.frankyang.pre.api.event;

/**
 * 委托的事件源，将所有访问委托至{@link DelegatedEventSources#getEventSources()}所返回的事件源。
 *
 * @param <T> 事件源所共有的事件类型。
 */
public interface DelegatedEventSources<T extends Event> extends EventSources<T> {
    EventSources<T> getEventSources();

    @Override
    default void create(String type) {
        getEventSources().create(type);
    }

    @Override
    default boolean trigger(String type, T event) {
        return getEventSources().trigger(type, event);
    }

    @Override
    default void subscribe(String type, EventListener<? super T> listener) {
        getEventSources().subscribe(type, listener);
    }

    @Override
    default void unsubscribe(String type, EventListener<? super T> listener) {
        getEventSources().unsubscribe(type, listener);
    }
}
