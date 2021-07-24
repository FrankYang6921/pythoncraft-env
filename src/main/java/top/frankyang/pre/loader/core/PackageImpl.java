package top.frankyang.pre.loader.core;

import com.google.gson.JsonParseException;
import top.frankyang.pre.loader.exceptions.PkgMetaException;
import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.misc.DummyFuture;
import top.frankyang.pre.python.internal.PythonExecutor;
import top.frankyang.pre.python.providers.PackagedProvider;
import top.frankyang.pre.util.JsonFiles;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Future;

public class PackageImpl implements Package {
    private final Path packageSrc;
    private final MetaData metaData;

    PackageImpl(Path packageRoot, Path packageSrc) {
        this.packageSrc = packageSrc;
        MetaDataWrapper wrapper;
        try {
            wrapper = JsonFiles.deserialize(
                packageRoot.resolve("meta.json").toFile(), MetaDataWrapper.class
            );
        } catch (IOException e) {
            throw new PkgMetaException("Cannot find meta.json, not a valid PythonCraft package.", e);
        } catch (JsonParseException e) {
            throw new PkgMetaException("Cannot parse meta.json, not a valid PythonCraft package.", e);
        }

        try {
            metaData = new MetaDataImpl(wrapper, packageRoot);
        } catch (NullPointerException e) {
            throw new PkgMetaException("Incomplete meta.json, not a valid PythonCraft package.", e);
        }
    }

    @Override
    public Path getPackageSrc() {
        return packageSrc;
    }

    @Override
    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean isPlaceholder() {
        return false;
    }

    @Override
    public Future<?> onConstruction(PythonExecutor executor) {
        Path path = getMetaData().getEntrypoint();
        return executor.submit(
            p -> p.execfile(path),
            new PackagedProvider(
                path.getParent().toString(),
                PythonCraft.getInstance()
                    .getPackageManager()
                    .getUserClassLoader(),
                this
            )
        );
    }

    @Override
    public Future<?> onDestruction(PythonExecutor executor) {
        return new DummyFuture();
    }
}
