package top.frankyang.pre.loader.core;

import java.nio.file.Path;

@SuppressWarnings("RedundantThrows")
public interface LoaderProxy {
    default void beforeLoad(PackageLoader loader) throws Throwable {
    }

    default void afterLoad(PackageLoader loader) throws Throwable {
    }

    default void afterInit(PackageLoader loader) throws Throwable {
    }

    default void onException(Path causeSource) {
    }
}
