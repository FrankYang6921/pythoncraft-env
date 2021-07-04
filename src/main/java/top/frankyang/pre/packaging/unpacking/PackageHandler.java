package top.frankyang.pre.packaging.unpacking;

import java.util.concurrent.Future;

public interface PackageHandler {
    Future<?> onConstruction(Package pkg);
    Future<?> onDestruction(Package pkg);
}
