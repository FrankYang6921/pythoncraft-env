package top.frankyang.pre.main;

import top.frankyang.pre.packaging.exceptions.ImpossibleException;
import top.frankyang.pre.packaging.exceptions.PackageInitializationException;
import top.frankyang.pre.packaging.exceptions.PackageConstructionException;
import top.frankyang.pre.packaging.exceptions.PermissionDeniedException;
import top.frankyang.pre.packaging.unpacking.PackageLoaderImpl;
import top.frankyang.pre.python.providers.PackagedProvider;
import top.frankyang.pre.packaging.unpacking.Package;
import top.frankyang.pre.packaging.unpacking.PackageLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

public class PythonCraft extends BasePythonCraft {
    private PythonCraft() {
        super();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static PythonCraft getInstance() {
        return PythonCraftSingleton.INSTANCE;
    }

    @Override
    protected PackageLoader loadPackages() {  // TODO Reload without the malformed package instead of crash.
        try {
            Files.createDirectories(Paths.get(envRoot, "scripts"));
        } catch (IOException e) {
            throw new PermissionDeniedException(
                "在创建.minecraft/scripts/时拒绝访问。", e
            );
        }

        PackageLoader loader = new PackageLoaderImpl(
            Paths.get(envRoot, "scripts"), this
        );
        try {
            loader.loadAll();
        } catch (PackageConstructionException e) {
            logger.error("上游加载器在加载包时出现了异常。", e);
            throw e;
        } catch (PackageInitializationException e) {
            logger.error("上游加载器在初始化包时出现了异常。", e);
            throw e;
        } catch (PermissionDeniedException e) {
            logger.error("上游加载器在访问文件时拒绝访问。", e);
            throw e;
        } catch (ImpossibleException e) {
            logger.error("上游加载器遇到了不可能发生的异常。这通常是由于一个模组或特定版本的JRE与PythonCraft冲突了。", e);
            throw e;
        } catch (Throwable throwable) {
            logger.fatal("出现了预料之外的异常，这通常不可能发生。", throwable);
            throw throwable;
        }

        return loader;
    }

    @Override
    public Future<?> onConstruction(Package pkg) {
        Path path = pkg.getMetaData().getEntrypointPath();

        return pythonThreadPool.submit(
            p -> p.execfile(path),
            new PackagedProvider(
                path, pkg.getMetaData().getClassProviders()
            )
        );
    }

    @Override
    public Future<?> onDestruction(Package pkg) {
        return null;
    }

    private static class PythonCraftSingleton {
        private static final PythonCraft INSTANCE = new PythonCraft();
    }
}
