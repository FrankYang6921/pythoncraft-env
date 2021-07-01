package top.frankyang.pre.loader;

import top.frankyang.pre.util.Digests;
import top.frankyang.pre.util.ZipFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipException;

public final class Packages {
    private Packages() {
    }

    public static Package get(String filename) {
        Path root;
        try {
            root = Files.createTempDirectory(Digests.digestMD5(filename) + "-");
        } catch (IOException e) {
            throw new RuntimeException("无法创建临时文件夹，您有足够的文件系统权限吗？", e);
        }
        // Uses util.ZipFiles to extract the package.
        try {
            ZipFiles.uncompress(new File(filename), root);
        } catch (ZipException e) {
            throw new RuntimeException("无法解压包，包使用了Zip的格式压缩吗？", e);
        } catch (IOException e) {
            throw new RuntimeException("无法读取包，您有足够的文件系统权限吗？", e);
        }

        root.toFile().deleteOnExit();  // Remember to release the resource!

        return new Package(root);
    }
}
