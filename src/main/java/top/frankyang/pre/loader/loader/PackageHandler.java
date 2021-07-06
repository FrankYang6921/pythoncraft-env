package top.frankyang.pre.loader.loader;

import java.util.concurrent.Future;

public interface PackageHandler {
    Future<?> onConstruction(Package pkg);
    Future<?> onDestruction(Package pkg);
}
