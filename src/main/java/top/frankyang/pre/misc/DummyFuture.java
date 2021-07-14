package top.frankyang.pre.misc;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DummyFuture implements Future<Object> {
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public Object get(long timeout, @NotNull TimeUnit unit) {
        return null;
    }
}
