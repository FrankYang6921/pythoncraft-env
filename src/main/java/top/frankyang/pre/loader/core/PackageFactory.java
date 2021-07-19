package top.frankyang.pre.loader.core;

import top.frankyang.pre.loader.exceptions.PkgMetaException;
import top.frankyang.pre.loader.exceptions.RuntimeIOException;
import top.frankyang.pre.util.Digests;
import top.frankyang.pre.util.ZipFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipException;

public final class PackageFactory {
    private PackageFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Package get(String filename) {
        Path root;
        try {
            root = Files.createTempDirectory(Digests.digestMD5(filename) + "-");
        } catch (IOException e) {
            throw new RuntimeIOException(
                "Access denied when creating temporary directory to extract the package.", e
            );
        }
        // Uses util.ZipFiles to extract the package.
        try {
            ZipFiles.uncompress(new File(filename), root);
        } catch (ZipException e) {
            throw new PkgMetaException(
                "Cannot extract the package. Is it a valid *NOT ENCRYPTED* ZIP file?", e
            );
        } catch (IOException e) {
            throw new RuntimeIOException(
                "Cannot extract the package. Do you have enough filesystem permission?", e
            );
        }

        root.toFile().deleteOnExit();  // Remember to release the resource!

        return new PackageImpl(root, Paths.get(filename));
    }

    public static Package get(Path filename) {
        return get(filename.toAbsolutePath().toString());
    }
}
