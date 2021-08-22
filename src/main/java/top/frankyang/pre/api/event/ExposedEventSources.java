package top.frankyang.pre.api.event;

import java.util.Map;

/**
 * 委托的事件源，将所有访问委托至{@link ExposedEventSources#getEventSources()}所返回的事件源。
 *
 * @param <T> 事件源所共有的事件类型。
 */
public interface ExposedEventSources<T extends Event> extends EventSources<T> {
    EventSources<T> getDelegate();

    @Override
    default void create(String type) {
        getDelegate().create(type);
    }

    @Override
    default int trigger(String type, T event) {
        return getDelegate().trigger(type, event);
    }

    @Override
    default void subscribe(String type, EventListener<? super T> listener) {
        getDelegate().subscribe(type, listener);
    }

    @Override
    default void unsubscribe(String type, EventListener<? super T> listener) {
        getDelegate().unsubscribe(type, listener);
    }

    @Override
    default Map<String, EventSource<T>> getEventSources() {
        return getDelegate().getEventSources();
    }
}
