package top.frankyang.pre.util;

import top.frankyang.pre.main.PythonCraft;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

public final class ClassLoaders {
    private ClassLoaders() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static URL[] pathsToURLs(List<Path> paths) {
        URL[] URLs = new URL[paths.size()];

        int i = 0;
        for (Path path : paths) {
            try {
                URLs[i++] = path.toUri().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid path to cast to URL: " + path, e);
            }
        }

        return URLs;
    }

    public static void injectToKnot(URLClassLoader classLoader, URL[] classURLs) {
        try {
            ClassLoader knotLoader = ClassLoaders.class.getClassLoader();

            PythonCraft.getInstance().getLogger().info(
                "Injecting to the class loader: `{}`.", knotLoader
            );

            Class<?> clazz;
            Field field;
            Method method;
            Object object;

            // Replaces fabric's `originalLoader` with customized class loader.
            clazz = knotLoader.getClass();
            field = clazz.getDeclaredField("originalLoader");
            field.setAccessible(true);
            field.set(knotLoader, classLoader);

            // Acquires fabric's `urlLoader` in order to add extra URLs to it.
            field = clazz.getDeclaredField("urlLoader");
            field.setAccessible(true);
            object = field.get(knotLoader);

            // Gets `urlLoader`'s `addURL` method in order to add the extra URLs.
            clazz = object.getClass();
            method = clazz.getMethod("addURL", URL.class);
            method.setAccessible(true);

            // Add the extra URLs!
            if (classURLs.length == 0) {
                PythonCraft.getInstance().getLogger().warn(
                    "No user URL class path registered. Skipping class path injection."
                );
            }
            for (URL classPath : classURLs) {
                method.invoke(object, classPath);
                PythonCraft.getInstance().getLogger().info(
                    "Injected a user URL class path: {}.", classPath
                );
            }

            PythonCraft.getInstance().getLogger().info(
                "Replaced original class loader: `{}`.", classLoader
            );
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
