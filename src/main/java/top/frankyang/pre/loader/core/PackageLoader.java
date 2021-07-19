package top.frankyang.pre.loader.core;

import top.frankyang.pre.loader.exceptions.*;
import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.misc.FileOnlyVisitor;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;

public class PackageLoader implements AutoCloseable, Closeable {
    private final PackageMap packages = new PackageMap();
    private final Path packagesRoot;
    private boolean isClosed = false;

    public PackageLoader(Path packagesRoot) {
        this.packagesRoot = packagesRoot;
        try {
            Files.createDirectories(packagesRoot);
        } catch (IOException e) {
            throw new RuntimeIOException(
                "Access denied when creating packages root.", e
            );
        }
    }

    public Collection<Package> getPackages() {
        return Collections.unmodifiableCollection(packages.packages());
    }

    public void loadFromRoot(LoaderProxy proxy) {
        try {
            proxy.beforeLoad(this);
            loadAll0();
            proxy.afterLoad(this);
            initialize();
            proxy.afterInit(this);
            isClosed = true;
        } catch (Throwable throwable) {
            handleException(throwable, proxy::onException);
            throw throwable;
        }
    }

    private void loadAll0() {
        if (isClosed)
            throw new IllegalStateException("The loader is already closed.");

        ArrayList<Path> paths = new ArrayList<>();

        try {
            Files.walkFileTree(
                packagesRoot, (FileOnlyVisitor<Path>) p -> {
                    if (p.getFileName().toString().endsWith(".zip")) paths.add(p);
                }
            );
        } catch (IOException e) {
            throw new RuntimeIOException(
                "Access denied when iterating packages root.", e
            );
        }

        PythonCraft.getInstance().getLogger().info("Loading " + paths.size() + " package(s).");

        for (Path path : paths) {
            PythonCraft.getInstance().getLogger().info(
                "Trying to load a package from \"" + path + "\"."
            );

            try {
                packages.put(PackageFactory.get(path));
            } catch (Exception e) {
                throw new PkgInitException(
                    "Cannot load the package from\"" + path + "\"!", e
                );
            }
        }

        paths.clear();

        try {  // Adds stub for disabled ones
            Files.walkFileTree(
                packagesRoot, (FileOnlyVisitor<Path>) p -> {
                    if (p.getFileName().toString().endsWith(".zip.disabled")) paths.add(p);
                }
            );
        } catch (IOException e) {
            throw new RuntimeIOException(
                "Access denied when iterating packages root.", e
            );
        }

        PythonCraft.getInstance().getLogger().info(paths.size() + " package(s) is/are disabled.");

        for (Path path : paths) {
            packages.put(new DummyPackage(path));
        }
    }

    private void initialize() {
        if (isClosed)
            throw new IllegalStateException("The loader is already closed.");

        HashMap<Package, Future<?>> futures = new HashMap<>();

        for (Package pkg : packages) {
            futures.put(pkg, pkg.onConstruction(PythonCraft.getInstance().getPythonThreadPool()));
        }

        futures.forEach((pkg, future) -> {
            try {
                future.get(60, SECONDS);
            } catch (InterruptedException e) {
                throw new ImpossibleException(
                    "The package loader thread got an external interrupt.", e
                );
            } catch (ExecutionException e) {
                throw new PkgLoadException(
                    "Package '" + pkg.getMetaData().getFullName() +
                        "' cannot be initialized since an exception is thrown by its entrypoint.", e, pkg.getPackageSrc()
                );
            } catch (TimeoutException e) {
                throw new PkgLoadException(
                    "Package '" + pkg.getMetaData().getFullName() +
                        "' cannot be initialized since its entrypoint blocked for over 1 minute.", e, pkg.getPackageSrc()
                );
            }
        });
    }

    private void unloadPackages() {
        List<Future<?>> futures = new LinkedList<>();

        for (Iterator<Package> iterator = packages.iterator(); iterator.hasNext(); ) {
            Package pkg = iterator.next();
            futures.add(pkg.onDestruction(PythonCraft.getInstance().getPythonThreadPool()));
            iterator.remove();
        }

        for (Future<?> future : futures) {
            try {
                future.get(60, SECONDS);
            } catch (Exception ignored) {
            }  // Ignores all the exceptions.
        }
    }

    private void handleException(Throwable throwable, Consumer<Path> srcConsumer) {
        if (throwable instanceof PackageException) {  // Gives reason
            srcConsumer.accept(((PackageException) throwable).getCauseSrc());
        } else {
            srcConsumer.accept(null);  // Won't give reason
        }
        isClosed = true;
        unloadPackages();
    }

    @Override
    public void close() {
        isClosed = true;
    }
}
