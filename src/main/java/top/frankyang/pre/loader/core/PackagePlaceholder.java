package top.frankyang.pre.loader.core;

import top.frankyang.pre.misc.DummyFuture;
import top.frankyang.pre.python.internal.PythonExecutor;

import java.nio.file.Path;
import java.util.concurrent.Future;

public class PackagePlaceholder implements Package {
    private final Path packageSrc;
    private final MetaData metaData;

    public PackagePlaceholder(Path packageSrc) {
        this.packageSrc = packageSrc;
        metaData = MetaDataPlaceholder.getInstance();
    }

    @Override
    public Path getPackageSrc() {
        return packageSrc;
    }

    @Override
    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public Future<?> onConstruction(PythonExecutor executor) {
        return new DummyFuture();
    }

    @Override
    public Future<?> onDestruction(PythonExecutor executor) {
        return new DummyFuture();
    }
}
