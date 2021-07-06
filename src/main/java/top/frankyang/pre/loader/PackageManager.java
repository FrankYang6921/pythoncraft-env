package top.frankyang.pre.loader;

import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import top.frankyang.pre.loader.exceptions.ImpossibleException;
import top.frankyang.pre.loader.exceptions.PkgInitException;
import top.frankyang.pre.loader.exceptions.PkgLoadException;
import top.frankyang.pre.loader.exceptions.RuntimeIOException;
import top.frankyang.pre.loader.loader.Package;
import top.frankyang.pre.loader.loader.PackageLoader;
import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.misc.FakeMetadata;
import top.frankyang.pre.util.ClassLoaders;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageManager {
    private final PackageLoader loader;

    private final List<ModNioResourcePack> userResourcePacks = new ArrayList<>();
    private final List<Path> userClassPaths = new ArrayList<>();
    private URLClassLoader userClassLoader = null;
    private boolean completelyLoaded = false;

    public PackageManager(PackageLoader loader) {
        this.loader = loader;
    }

    public void construct() {
        try {
            construct0();
        } catch (PkgInitException e) {
            PythonCraft.getLogger().error("An exception occurred when constructing a package in the package loader.", e);
            throw e;
        } catch (PkgLoadException e) {
            PythonCraft.getLogger().error("An exception occurred when initializing a package in the package loader.", e);
            throw e;
        } catch (RuntimeIOException e) {
            PythonCraft.getLogger().error("A file operation was denied in the package loader.", e);
            throw e;
        } catch (ImpossibleException e) {
            PythonCraft.getLogger().error("An impossible exception occurred in the package loader.", e);
            throw e;
        } catch (Throwable throwable) {
            PythonCraft.getLogger().fatal("An unexpected exception occurred in the package loader.", throwable);
            throw throwable;
        }
    }

    private void construct0() {
        if (completelyLoaded)
            throw new IllegalStateException("Already completely loaded.");

        loader.loadAll(this::cleanupEverything);

        for (Package p : loader.getPackages()) {  // Iterate over all the packages
            for (Path asset : p.getMetaData().getAssetPaths()) {  // Add the asset paths
                userResourcePacks.add(new ModNioResourcePack(
                    new FakeMetadata(p.getMetaData().getIdentifier()), asset, null, null, true
                ));
                try {
                    Files.createFile(asset.resolve("fabric.mod.json"));  // Fools fabric ;)
                } catch (IOException e) {
                    throw new RuntimeIOException();
                }
            }
            userClassPaths.addAll(p.getMetaData().getClassPaths());  // Add the class paths
        }

        URL[] userClassURLs = ClassLoaders.pathsToURLs(userClassPaths);
        userClassLoader = new URLClassLoader(
            userClassURLs, ClassLoader.getSystemClassLoader());

        loader.initAll(this::cleanupEverything);

        ClassLoaders.injectToKnot(userClassLoader, userClassURLs);  // Only runs when successful

        completelyLoaded = true;
    }

    public PackageLoader getLoader() {
        return loader;
    }

    public URLClassLoader getUserClassLoader() {
        return userClassLoader;
    }

    public List<Path> getUserClassPaths() {
        return Collections.unmodifiableList(userClassPaths);
    }

    public List<ModNioResourcePack> getUserResourcePacks() {
        return Collections.unmodifiableList(userResourcePacks);
    }

    public boolean isCompletelyLoaded() {
        return completelyLoaded;
    }

    private void cleanupEverything(Path cause) {
        userClassLoader = null;
        userClassPaths.clear();
        userResourcePacks.clear();
    }
}
