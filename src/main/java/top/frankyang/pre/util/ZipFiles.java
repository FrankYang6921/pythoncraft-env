package top.frankyang.pre.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ZipFiles {
    private ZipFiles() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void uncompress(File srcFile, Path dstPath) throws IOException {
        try (ZipFile zipFile = new ZipFile(srcFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                if (entry.isDirectory()) {
                    Files.createDirectories(
                        dstPath.resolve(entry.getName())
                    );
                } else {
                    Files.copy(
                        zipFile.getInputStream(entry), dstPath.resolve(entry.getName())
                    );
                }
            }
        }
    }
}
