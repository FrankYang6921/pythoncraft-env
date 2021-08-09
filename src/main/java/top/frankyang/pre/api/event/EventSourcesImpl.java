package top.frankyang.pre.api.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventSourcesImpl<T extends Event> implements EventSources<T> {
    private final Map<String, EventSource<T>> eventSources = new HashMap<>();

    @Override
    public void create(String type) {
        eventSources.put(type, new EventSource<>(type));
    }

    @Override
    public boolean trigger(String type, T event) {
        return eventSources.computeIfAbsent(type, EventSource::new).trigger(event);
    }

    @Override
    public void subscribe(String type, EventListener<? super T> listener) {
        Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).subscribe(listener);
    }

    @Override
    public void unsubscribe(String type, EventListener<? super T> listener) {
        Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).unsubscribe(listener);
    }
}
