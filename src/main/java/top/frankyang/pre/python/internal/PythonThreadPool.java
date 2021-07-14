package top.frankyang.pre.python.internal;

import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PythonThreadPool implements PythonExecutor {
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

    public Future<?> submit(Consumer<? super Python> task, PythonProvider provider) {
        return executor.submit(() -> {
            Python p = provider.newPython();
            try {
                task.accept(p);
                provider.whenResolved(p);
            } catch (Exception e) {
                provider.whenRejected(e);
            } finally {
                provider.whenFinished(p);
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
