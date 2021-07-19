package top.frankyang.pre.main;

import top.frankyang.pre.api.Minecraft;
import top.frankyang.pre.gui.PackageExceptionFrame;
import top.frankyang.pre.loader.PackageManager;
import top.frankyang.pre.loader.core.Pack;
import top.frankyang.pre.loader.core.PackageLoader;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

public class PythonCraft extends AbstractPythonCraft {
    private PackageManager packageManager;

    private PythonCraft() {
        super();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static PythonCraft getInstance() {
        return PythonCraftSingleton.INSTANCE;
    }

    private void initialize() {
        packageManager = new PackageManager(() ->
            new PackageLoader(Minecraft.getPackagesPath())
        );
        packageManager.tryToConstruct(PackageExceptionFrame::open);
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
