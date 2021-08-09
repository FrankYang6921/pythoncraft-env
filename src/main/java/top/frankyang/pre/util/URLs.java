package top.frankyang.pre.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class URLs {
    private URLs() {
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

    public static Path[] URLsToPaths(List<URL> urls) {
        Path[] paths = new Path[urls.size()];

        int i = 0;
        for (URL url : urls) {
            try {
                paths[i++] = Paths.get(url.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid URL to cast to path: " + url, e);
            }
        }

        return paths;
    }
}
