package top.frankyang.pre.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public final class DynamicJarLoader {
    private DynamicJarLoader() {
    }

    public static void loadJar(String... filenames) {
        URL[] jarUrls;
        try {
            Arrays.stream(filenames).map(URL::new).toArray();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cannot load classes from the JAR file due to malformed file URL.", e);
        }
        URLClassLoader classLoader = new URLClassLoader(jarUrls);
    }
}
