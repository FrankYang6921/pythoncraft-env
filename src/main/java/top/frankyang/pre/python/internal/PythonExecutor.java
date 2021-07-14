package top.frankyang.pre.python.internal;

import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface PythonExecutor {
    Future<?> submit(Consumer<? super Python> task, PythonProvider factory);
}
