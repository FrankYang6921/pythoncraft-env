package top.frankyang.pre.api.event;

import java.util.Map;

/**
 * 委托的事件源，将所有访问委托至{@link ExposedEventSources#getBackingMap()}所返回的事件源。
 *
 * @param <T> 事件源所共有的事件类型。
 */
public interface ExposedEventSources<T extends Event> extends EventSources<T> {
    EventSources<T> getDelegateEventSources();

    @Override
    default void create(String type) {
        getDelegateEventSources().create(type);
    }

    @Override
    default int trigger(String type, T event) {
        return getDelegateEventSources().trigger(type, event);
    }

    @Override
    default void subscribe(String type, EventListener<? super T> listener) {
        getDelegateEventSources().subscribe(type, listener);
    }

    @Override
    default void unsubscribe(String type, EventListener<? super T> listener) {
        getDelegateEventSources().unsubscribe(type, listener);
    }

    @Override
    default Map<String, EventSource<T>> getBackingMap() {
        return getDelegateEventSources().getBackingMap();
    }
}
