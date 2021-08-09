package top.frankyang.pre.api.event;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 事件监听器。
 *
 * @param <T> 要侦听的事件类型。
 */
@FunctionalInterface
public interface EventListener<T extends Event> {
    static EventListener<Event> of(Function<Event, Boolean> target) {
        return target::apply;
    }

    static EventListener<Event> of(Supplier<Boolean> target) {
        return e -> target.get();
    }

    static EventListener<Event> of(Consumer<Event> target) {
        return e -> {
            target.accept(e);
            return true;
        };
    }

    static EventListener<Event> of(Runnable target) {
        return e -> {
            target.run();
            return true;
        };
    }

    /**
     * 侦听事件的回调。
     *
     * @param t 事件。
     * @return 该事件是否处理成功。
     * @throws Throwable 如果事件处理过程中出现了任何错误。
     */
    boolean trigger(T t) throws Throwable;
}
