package top.frankyang.pre.loader.loader;

import top.frankyang.pre.loader.exceptions.ImpossibleException;
import top.frankyang.pre.loader.exceptions.PkgInitException;
import top.frankyang.pre.loader.exceptions.PkgLoadException;
import top.frankyang.pre.loader.exceptions.RuntimeIOException;
import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.misc.FileOnlyVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public abstract class PackageLoader {
    private final Path scriptsRoot;
    private final List<Package> packages = new ArrayList<>();
    private Path currentlyHandling;


    public PackageLoader(Path scriptsRoot) {
        this.scriptsRoot = scriptsRoot;
        try {
            Files.createDirectories(scriptsRoot);
        } catch (IOException e) {
            throw new RuntimeIOException(
                "Access denied when creating scripts root.", e
            );
        }
    }

    public List<Package> getPackages() {
        return Collections.unmodifiableList(packages);
    }

    public void loadAll(Consumer<Path> onException) {
        try {
            loadAll0();
        } catch (Throwable throwable) {
            onException.accept(currentlyHandling);
            unloadOnFailure();
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
            throw new RuntimeIOException(
                "Access denied when iterating scripts root.", e
            );
        }

        PythonCraft.getLogger().info("Loading " + paths.size() + " package(s).");

        for (Path path : paths) {
            PythonCraft.getLogger().info(
                "Trying to load a package from \"" + path + "\"."
            );

            try {
                packages.add(PackageFactory.get(currentlyHandling = path));
            } catch (Exception e) {
                throw new PkgInitException(
                    "Cannot load the package from\"" + path + "\"!", e
                );
            }
        }
        currentlyHandling = null;
    }

    public void initAll(Consumer<Path> onException) {
        try {
            initAll0();
        } catch (Throwable throwable) {
            onException.accept(currentlyHandling);
            unloadOnFailure();
            throw throwable;
        }
    }

    private void initAll0() {
        List<Future<?>> futures = new LinkedList<>();

        for (Package pkg : packages) {
            futures.add(onConstruction(pkg));
        }

        int i = 0;
        for (Future<?> future : futures) {
            Package pkg = packages.get(i++);

            try {
                currentlyHandling = pkg.getPackageSrc();
                future.get(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new ImpossibleException(
                    "The package loader thread got an external interrupt.", e
                );
            } catch (ExecutionException e) {
                throw new PkgLoadException(
                    "Package" + pkg.getFullName() +
                        "cannot be initialized since an exception is thrown by its entrypoint.", e
                );
            } catch (TimeoutException e) {
                throw new PkgLoadException(
                    "Package" + pkg.getFullName() +
                        "cannot be initialized since its entrypoint blocked for over 1 minute.", e
                );
            }
        }
        currentlyHandling = null;
    }

    public void unloadOnFailure() {
        List<Future<?>> futures = new LinkedList<>();

        for (Iterator<Package> iterator = packages.iterator(); iterator.hasNext(); ) {
            Package pkg = iterator.next();
            futures.add(onDestruction(pkg));
            iterator.remove();
        }

        for (Future<?> future : futures) {
            try {
                future.get(60, TimeUnit.SECONDS);
            } catch (Exception ignored) {
            }  // Ignores all the exceptions.
        }
    }

    protected abstract Future<?> onConstruction(Package pkg);

    protected abstract Future<?> onDestruction(Package pkg);
}
