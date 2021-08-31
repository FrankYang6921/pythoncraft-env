package top.frankyang.pre.api.event;

/**
 * 委托的事件源，将所有访问委托至{@link ExposedEventBus#getDelegateEventSources()}所返回的事件源。
 *
 * @param <T> 事件源所共有的事件类型。
 */
public interface ExposedEventBus<T extends Event> extends EventBus<T> {
    EventBus<T> getDelegateEventSources();

    @Override
    default EventSource<T> newSource(String type) {
        return getDelegateEventSources().newSource(type);
    }

    @Override
    default EventSource<T> getSource(String type) {
        return getDelegateEventSources().getSource(type);
    }

    @Override
    default int trigger(String type, T event) {
        return getDelegateEventSources().trigger(type, event);
    }

    @Override
    default EventBus<T> subscribe(String type, EventListener<? super T> listener) {
        getDelegateEventSources().subscribe(type, listener);
        return this;
    }

    @Override
    default EventBus<T> unsubscribe(String type, EventListener<? super T> listener) {
        getDelegateEventSources().unsubscribe(type, listener);
        return this;
    }
}
