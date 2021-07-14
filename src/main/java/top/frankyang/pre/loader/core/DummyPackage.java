package top.frankyang.pre.loader.core;

import top.frankyang.pre.misc.DummyFuture;
import top.frankyang.pre.python.internal.PythonExecutor;

import java.nio.file.Path;
import java.util.concurrent.Future;

public class DummyPackage implements Package {
    private final Path packageRoot;
    private final Path packageSrc;
    private final MetaData metaData;

    public DummyPackage(Path src) {
        packageSrc = src;
        packageRoot = src.getParent();
        metaData = MetaData.dummy(src);
    }

    @Override
    public Path getPackageRoot() {
        return packageRoot;
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
    public boolean isDummy() {
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
