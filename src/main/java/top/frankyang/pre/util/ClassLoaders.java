package top.frankyang.pre.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public final class ClassLoaders {
    private ClassLoaders() {
    }

    public static URLClassLoader get(String... URLs) {
        URL[] jarURLs = (URL[]) Arrays.stream(URLs)
            .map(s -> {
                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            })
            .toArray();

        return new URLClassLoader(jarURLs, Thread.currentThread().getContextClassLoader());
    }
}
