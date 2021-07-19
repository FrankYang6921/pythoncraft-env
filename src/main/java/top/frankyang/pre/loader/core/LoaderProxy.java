package top.frankyang.pre.loader.core;

import java.nio.file.Path;

public interface LoaderProxy {
    default void beforeLoad(PackageLoader loader) {
    }

    default void afterLoad(PackageLoader loader) {
    }

    default void afterInit(PackageLoader loader) {
    }

    default void onException(Path causeSource) {
    }
}
