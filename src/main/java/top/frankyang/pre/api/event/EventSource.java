package top.frankyang.pre.api.event;

import top.frankyang.pre.main.PythonCraft;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 事件源。事件源允许您添加多个侦听器，并通过事件源触发某一事件。
 *
 * @param <T> 要监听和触发的事件类型。
 */
public class EventSource<T extends Event> {
    private final List<EventListener<? super T>> eventListeners = new CopyOnWriteArrayList<>();
    private final String name;

    public EventSource(String name) {
        this.name = name;
    }

    /**
     * 订阅一个事件源。当事件源收到触发时，回调该侦听器。
     *
     * @param listener 用于订阅事件的事件侦听器。
     */
    public void subscribe(EventListener<? super T> listener) {
        eventListeners.add(listener);
    }

    /**
     * 取消订阅一个事件源。
     *
     * @param listener 要取消订阅的事件侦听器。
     */
    public void unsubscribe(EventListener<? super T> listener) {
        eventListeners.remove(listener);
    }

    /**
     * 触发一个事件源。
     *
     * @param event 用于触发事件源的事件。
     * @return 成功触发的侦听器的数量。
     */
    public int trigger(T event) {
        int c = 0;
        for (EventListener<? super T> eventListener : eventListeners) {
            try {
                eventListener.trigger(event);
            } catch (Throwable throwable) {
                PythonCraft.getInstance().getLogger().error("Error in event source '" + name + "': ", throwable);
                break;
            } finally {
                c++;
            }
        }
        return c;
    }

    public int getListenerCount() {
        return eventListeners.size();
    }

    public String getName() {
        return name;
    }
}
