package top.frankyang.pre.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

public final class ClassLoaders {
    private ClassLoaders() {
    }

    public static URLClassLoader get(List<Path> paths) {
        URL[] URLs = new URL[paths.size()];

        int i = 0;
        for (Path path : paths) {
            try {
                URLs[i++] = path.toUri().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException("不能将路径转化为URL：" + path, e);
            }
        }

        return new URLClassLoader(URLs, Thread.currentThread().getContextClassLoader());
    }
}
