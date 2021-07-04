package top.frankyang.pre.packaging.unpacking;

import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.packaging.exceptions.*;
import top.frankyang.pre.misc.FileOnlyVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class PackageLoader {
    private final Path scriptsRoot;
    private final List<Package> packages = new ArrayList<>();

    public PackageLoader(Path scriptsRoot) {
        this.scriptsRoot = scriptsRoot;
    }

    public List<Package> getPackages() {
        return Collections.unmodifiableList(packages);
    }

    public void loadAll() {
        try {
            loadAll0();
        } catch (Throwable throwable) {
            packages.clear(); // Ensures nothing is loaded improperly.
            throw throwable;
        }
    }

    private void loadAll0() {
        packages.clear();  // Ensures nothing is loaded twice.

        List<Path> paths = new ArrayList<>();

        try {
            Files.walkFileTree(
                scriptsRoot, (FileOnlyVisitor<Path>) p -> {
                    if (p.getFileName().toString().endsWith(".zip")) paths.add(p);
                }
            );
        } catch (IOException e) {
            throw new PermissionDeniedException("在遍历.minecraft/scripts/时拒绝访问。");
        }

        List<Future<?>> futures = new LinkedList<>();

        PythonCraft.getLogger().info("正在加载以下的包：" + paths.toString());

        for (Path path : paths) {
            PythonCraft.getLogger().info("正在加载来自\"" + path + "\"的PythonCraft包。");

            Package pkg;
            Future<?> ftr;

            try {
                pkg = PackageFactory.get(path.toAbsolutePath().toString());
                ftr = onConstruction(pkg);
            } catch (Exception e) {
                throw new PackageConstructionException(
                    "无法加载来自\"" + path + "\"的PythonCraft包。", e
                );
            }

            futures.add(ftr);
            packages.add(pkg);
        }

        int i = 0;
        for (Future<?> future : futures) {
            Package pkg = packages.get(i++);

            try {
                future.get(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new ImpossibleException(
                    "PythonCraft包加载器遭到了外部的中断。", e
                );
            } catch (ExecutionException e) {
                throw new PackageInitializationException(
                    "包" + pkg.getFullName() + "无法被初始化，它的入口模块抛出了如下异常：", e
                );
            } catch (TimeoutException e) {
                throw new PackageInitializationException(
                    "包" + pkg.getFullName() + "无法被初始化，它的入口模块阻塞了超过60s。", e
                );
            }
        }
    }

    protected abstract Future<?> onConstruction(Package pkg);
    protected abstract Future<?> onDestruction(Package pkg);
}
