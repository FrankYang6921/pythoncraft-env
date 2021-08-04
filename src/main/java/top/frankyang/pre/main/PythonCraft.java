package top.frankyang.pre.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.frankyang.pre.api.Minecraft;
import top.frankyang.pre.gui.swing.PackageExceptionFrame;
import top.frankyang.pre.loader.PackageManager;
import top.frankyang.pre.loader.core.Pack;
import top.frankyang.pre.loader.core.PackageLoader;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

public final class PythonCraft extends AbstractPythonCraft {
    private final Logger logger = LogManager.getLogger();
    private PackageManager packageManager;

    private PythonCraft() {
        super();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static PythonCraft getInstance() {
        return PythonCraftSingleton.INSTANCE;
    }

    protected void initialize() {
        packageManager = new PackageManager(() ->
            new PackageLoader(Minecraft.getPackagesPath())
        );
        packageManager.tryToConstruct(PackageExceptionFrame::open);
    }

    public Logger getLogger() {
        return this.logger;
    }

    public URLClassLoader getUserClassLoader() {
        return packageManager.getUserClassLoader();
    }

    public List<Path> getUserClassPaths() {
        return packageManager.getUserClassPaths();
    }

    public List<Pack> getUserResourcePacks() {
        return packageManager.getUserResourcePacks();
    }

    public PackageManager getPackageManager() {
        return this.packageManager;
    }

    private static class PythonCraftSingleton {
        private static final PythonCraft INSTANCE = new PythonCraft();

        static {
            INSTANCE.initialize();
        }
    }
}
