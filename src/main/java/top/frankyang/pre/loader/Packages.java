package top.frankyang.pre.loader;

import top.frankyang.pre.util.Digests;
import top.frankyang.pre.util.ZipFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Packages {
    private Packages() {
    }

    public static Package get(String filename) throws IOException {
        Path root = Files.createTempDirectory(Digests.digestMD5(filename) + "-");
        // Uses util.ZipFiles to extract the package.
        ZipFiles.uncompress(new File(filename), root);
        root.toFile().deleteOnExit();  // Remember to release the resource!

        return new Package(root);
    }
}
