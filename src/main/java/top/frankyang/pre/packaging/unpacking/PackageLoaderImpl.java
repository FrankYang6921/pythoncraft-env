package top.frankyang.pre.packaging.unpacking;

import java.nio.file.Path;
import java.util.concurrent.Future;

public class PackageLoaderImpl extends PackageLoader {
    private final PackageHandler handler;

    public PackageLoaderImpl(Path packageHome, PackageHandler handler) {
        super(packageHome);
        this.handler = handler;
    }

    @Override
    protected Future<?> onConstruction(Package pkg) {
        return handler.onConstruction(pkg);
    }

    @Override
    protected Future<?> onDestruction(Package pkg) {
        return handler.onDestruction(pkg);
    }
}
