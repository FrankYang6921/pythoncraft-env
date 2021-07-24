package top.frankyang.pre.loader.core;

import top.frankyang.pre.python.internal.PythonExecutor;

import java.nio.file.Path;
import java.util.concurrent.Future;

public interface Package {
    Path getPackageSrc();

    MetaData getMetaData();

    boolean isPlaceholder();

    Future<?> onConstruction(PythonExecutor executor);

    Future<?> onDestruction(PythonExecutor executor);
}
