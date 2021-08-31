package top.frankyang.pre.api.event;

import java.util.*;

public class EventBusImpl<T extends Event> implements EventBus<T> {
    public final Map<String, EventSource<T>> eventSources = new HashMap<>();

    public EventBusImpl(String... types) {
        Arrays.stream(types).forEach(this::newSource);
    }

    @Override
    public EventSource<T> newSource(String type) {
        return eventSources.computeIfAbsent(type, EventSource::new);
    }

    @Override
    public EventSource<T> getSource(String type) {
        return eventSources.get(type);
    }

    @Override
    public int trigger(String type, T event) {
        return Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).trigger(event);
    }

    @Override
    public EventBus<T> subscribe(String type, EventListener<? super T> listener) {
        Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).subscribe(listener);
        return this;
    }

    @Override
    public EventBus<T> unsubscribe(String type, EventListener<? super T> listener) {
        Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).unsubscribe(listener);
        return this;
    }
}
