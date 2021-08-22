package top.frankyang.pre.api.event;

import java.util.*;

public class EventSourcesImpl<T extends Event> implements EventSources<T> {
    public final Map<String, EventSource<T>> eventSources = new HashMap<>();

    public EventSourcesImpl(String... types) {
        Arrays.stream(types).forEach(this::create);
    }

    @Override
    public void create(String type) {
        eventSources.put(type, new EventSource<>(type));
    }

    @Override
    public int trigger(String type, T event) {
        return Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).trigger(event);
    }

    @Override
    public void subscribe(String type, EventListener<? super T> listener) {
        Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).subscribe(listener);
    }

    @Override
    public void unsubscribe(String type, EventListener<? super T> listener) {
        Objects.requireNonNull(eventSources.get(type), "No such event type present: " + type).unsubscribe(listener);
    }

    public Map<String, EventSource<T>> getEventSources() {
        return Collections.unmodifiableMap(eventSources);
    }

    @Override
    public String toString() {
        return "EventSourcesImpl{" +
            "eventSources=" + eventSources +
            '}';
    }
}
