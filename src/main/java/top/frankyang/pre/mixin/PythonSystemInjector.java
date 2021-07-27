package top.frankyang.pre.mixin;

import jnr.posix.util.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.core.PrePy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import top.frankyang.pre.api.Minecraft;
import top.frankyang.pre.loader.exceptions.RuntimeIOException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = PrePy.class, remap = false)
public class PythonSystemInjector {
    private static final Logger LOGGER = LogManager.getLogger("EarlyJython");

    @Shadow
    private static URL tweakWindowsFileURL(URL url) throws MalformedURLException {
        return null;
    }

    private static URI toDefaultFileSystem(URI uri) throws IOException {
        Path dst = Minecraft.getGamePath().resolve(".jython-runtime.jar");
        if (!Files.exists(dst)) {
            JOptionPane.showMessageDialog(
                null, "这是您第一次使用PythonCraft Runtime Environment。由于需要解压运行时文件，游戏启动可能较慢，请耐心等待，" +
                    "后续的启动速度会显著提升。", "PythonCraft Runtime Environment", JOptionPane.INFORMATION_MESSAGE
            );
            Files.copy(Paths.get(uri), dst, StandardCopyOption.REPLACE_EXISTING);
        }
        return dst.toUri();
    }

    /**
     * @reason ...
     * @author kworker
     */
    @Overwrite
    public static String getJarFileNameFromURL(URL url) {
        URI fileURI = null;

        try {
            String protocol = url == null ? "" : url.getProtocol();
            switch (protocol) {
                case "jar":
                    if (Platform.IS_WINDOWS) {
                        url = tweakWindowsFileURL(url);
                    }

                    URLConnection c = Objects.requireNonNull(url).openConnection();
                    fileURI = ((JarURLConnection) c).getJarFileURL().toURI();
                    break;
                case "vfs":
                case "vfszip":
                    String path = url.getPath();
                    Pattern p = Pattern.compile("/([^./]+\\.jar)/org/python/core/\\w+.class");
                    Matcher m = p.matcher(path);
                    if (m.find()) {
                        fileURI = (new URL("file:" + path.substring(0, m.end(1)))).toURI();
                    }
            }
        } catch (URISyntaxException | IllegalArgumentException | IOException ignored) {
        }

        if (fileURI == null) {
            LOGGER.warn("Cannot locate the Jython jar for site packages.");
            return null;
        }
        try {
            LOGGER.info("Copying the memory file system to the default file system.");
            if (!fileURI.getScheme().equals("file"))
                fileURI = toDefaultFileSystem(fileURI);
            return new File(fileURI).toString();
        } catch (IOException e) {
            LOGGER.error("Failed to copy the memory file system due to exception: ", e);
            throw new RuntimeIOException(e);
        }
    }
}
