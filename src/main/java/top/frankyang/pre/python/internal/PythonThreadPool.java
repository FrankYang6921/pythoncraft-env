package top.frankyang.pre.python.internal;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class PythonThreadPool {
    private final ThreadPoolExecutor executor;

    public PythonThreadPool(int minPythonCount, int maxPythonCount, long keepAliveTime, TimeUnit unit) {
        executor = new ThreadPoolExecutor(
            minPythonCount,
            maxPythonCount,
            keepAliveTime,
            unit,
            new SynchronousQueue<>(),  // Ensures nothing is going to be queued.
            new PythonThreadFactory(),  // Ensures to create a "Python" thread.
            new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public Future<?> submit(Consumer<? super Python> task, PythonProvider factory) {
        return executor.submit(() -> {
            Python p = factory.newPython();
            try {
                task.accept(p);
                factory.whenResolved(p);
            } catch (Exception e) {
                factory.whenRejected(e);
            } finally {
                factory.whenFinished(p);
                p.close();
            }
        });
    }

    public int getMinPythonCount() {
        return executor.getCorePoolSize();
    }

    public void setMinPythonCount(int corePoolSize) {
        executor.setCorePoolSize(corePoolSize);
    }

    public int getMaxPythonCount() {
        return executor.getMaximumPoolSize();
    }

    public void setMaxPythonCount(int maximumPoolSize) {
        executor.setMaximumPoolSize(maximumPoolSize);
    }

    public void setKeepAliveTime(long time, TimeUnit unit) {
        executor.setKeepAliveTime(time, unit);
    }

    public long getKeepAliveTime(TimeUnit unit) {
        return executor.getKeepAliveTime(unit);
    }
}
