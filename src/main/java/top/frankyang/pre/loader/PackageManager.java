package top.frankyang.pre.loader;

import top.frankyang.pre.gui.PackageManagerGui;
import top.frankyang.pre.loader.core.Package;
import top.frankyang.pre.loader.core.*;
import top.frankyang.pre.loader.exceptions.ImpossibleException;
import top.frankyang.pre.loader.exceptions.PkgInitException;
import top.frankyang.pre.loader.exceptions.PkgLoadException;
import top.frankyang.pre.loader.exceptions.RuntimeIOException;
import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.misc.LambdaContainer;
import top.frankyang.pre.util.ClassLoaders;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class PackageManager {
    private final Supplier<PackageLoader> loaderSupplier;
    private final List<Pack> userResourcePacks = new ArrayList<>();
    private final List<Path> userClassPaths = new ArrayList<>();
    private Path causeSource;
    private ManagerStatus managerStatus = ManagerStatus.EMPTY;
    private PackageLoader packageLoader;
    private URLClassLoader userClassLoader;
    private volatile boolean managerFrameOpen;


    public PackageManager(Supplier<PackageLoader> loaderSupplier) {
        this.loaderSupplier = loaderSupplier;
    }

    public URLClassLoader getUserClassLoader() {
        if (managerStatus == ManagerStatus.EMPTY || managerStatus == ManagerStatus.FAILED)
            throw new IllegalStateException("This manager is not loaded or failed.");
        return userClassLoader;
    }

    public List<Path> getUserClassPaths() {
        if (managerStatus == ManagerStatus.EMPTY || managerStatus == ManagerStatus.FAILED)
            throw new IllegalStateException("This manager is not loaded or failed.");
        return Collections.unmodifiableList(userClassPaths);
    }

    public List<Pack> getUserResourcePacks() {
        if (managerStatus == ManagerStatus.EMPTY || managerStatus == ManagerStatus.FAILED)
            throw new IllegalStateException("This manager is not loaded or failed.");
        return Collections.unmodifiableList(userResourcePacks);
    }

    public void openManagerFrame() {
        if (managerStatus == ManagerStatus.EMPTY || managerStatus == ManagerStatus.FAILED)
            throw new IllegalStateException("This manager is not loaded or failed.");

        PackageManagerGui.open(packageLoader.getPackages().stream().map(PackageInfo::new));
    }

    public void construct() {
        try {
            managerStatus = ManagerStatus.LOADING;
            construct0();
        } catch (PkgInitException e) {
            PythonCraft.getInstance().getLogger().error("An exception occurred when constructing a package in the package loader.", e);
            throw e;
        } catch (PkgLoadException e) {
            PythonCraft.getInstance().getLogger().error("An exception occurred when initializing a package in the package loader.", e);
            throw e;
        } catch (RuntimeIOException e) {
            PythonCraft.getInstance().getLogger().error("A file operation was denied in the package loader.", e);
            throw e;
        } catch (ImpossibleException e) {
            PythonCraft.getInstance().getLogger().error("An impossible exception occurred in the package loader.", e);
            throw e;
        } catch (Throwable throwable) {
            PythonCraft.getInstance().getLogger().fatal("An unexpected exception occurred in the package loader.", throwable);
            throw throwable;
        }
    }

    public void tryToConstruct(BiFunction<Throwable, Path, ExceptionStatus> condition) {
        while (managerStatus != ManagerStatus.LOADED) {
            try {
                construct();
            } catch (Throwable throwable) {
                switch (condition.apply(throwable, causeSource)) {
                    case IGNORE:  // Reload all the packages again
                        continue;
                    case DISABLE:  // Disable the malformed package
                        disableTheCause();
                        break;
                    case CRASH:  // Crash the game
                        throw throwable;
                }
            }
        }
    }

    private void construct0() {
        if (managerStatus == ManagerStatus.LOADED)
            throw new IllegalStateException("This manager is already loaded.");

        LambdaContainer<URL[]> userClassURLs = new LambdaContainer<>();

        try (PackageLoader loader = loaderSupplier.get()) {
            loader.loadFromRoot(new LoaderProxy() {
                @Override
                public void afterLoad(PackageLoader loader) throws IOException {
                    userClassURLs.set(prepareUserStuffs(loader));
                }

                @Override
                public void afterInit(PackageLoader loader) {
                    packageLoader = loader;
                }

                @Override
                public void onException(Path causeSource) {
                    resetStates(causeSource);
                }
            });
        }

        ClassLoaders.injectToKnot(userClassLoader, userClassURLs.get());  // Only runs when successful

        managerStatus = ManagerStatus.LOADED;
    }

    private void disableTheCause() {
        try {
            Files.move(causeSource, causeSource.resolveSibling(causeSource.getFileName().toString() + ".disabled"));
        } catch (IOException e) {
            throw new RuntimeIOException("Cannot disable a certain package.", e);
        }
    }

    private URL[] prepareUserStuffs(PackageLoader loader) throws IOException {
        Path path = Files.createTempDirectory("thumbnail-");

        for (Package p : loader.getPackages()) {  // Iterate over all the packages
            for (Path asset : p.getMetaData().getAssetPaths()) {  // Add the asset paths as packs.
                userResourcePacks.add(
                    new Pack(p.getMetaData().getIdentifier(), asset)
                );
                Files.createFile(asset.resolve("fabric.mod.json"));
            }
            if (p.getMetaData().hasThumbnail()) {
                Path textures = Files.createDirectories(
                    path.resolve("assets").resolve(p.getMetaData().getIdentifier())
                );
                Files.copy(
                    p.getMetaData().getThumbnailPath(), textures.resolve("thumbnail.png")
                );
            }
            userClassPaths.addAll(p.getMetaData().getClassPaths());  // Add the class paths
        }

        userResourcePacks.add(new Pack("pre", path));
        Files.createFile(path.resolve("fabric.mod.json"));
        path.toFile().deleteOnExit();

        URL[] userClassURLs = ClassLoaders.pathsToURLs(userClassPaths);
        userClassLoader = new URLClassLoader(
            userClassURLs, ClassLoader.getSystemClassLoader());
        return userClassURLs;
    }

    private void resetStates(Path causeSrc) {
        userResourcePacks.clear();
        userClassPaths.clear();
        userClassLoader = null;
        causeSource = causeSrc;
        managerStatus = ManagerStatus.FAILED;
    }

    public ManagerStatus getManagerStatus() {
        return this.managerStatus;
    }
}
