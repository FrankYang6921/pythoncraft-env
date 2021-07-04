package top.frankyang.pre.python.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PythonThreadFactory implements ThreadFactory {
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    public PythonThreadFactory() {
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(Thread.currentThread().getThreadGroup(), r,
            String.format("python-%d", threadNumber.getAndIncrement()),
            0);
        if (!t.isDaemon())
            t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
