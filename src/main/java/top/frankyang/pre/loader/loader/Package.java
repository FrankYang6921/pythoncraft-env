package top.frankyang.pre.loader.loader;

import com.google.gson.JsonParseException;
import top.frankyang.pre.loader.exceptions.PkgMetaException;
import top.frankyang.pre.util.JsonFiles;

import java.io.IOException;
import java.nio.file.Path;

public class Package {
    private final Path packageRoot;
    private final Path packageSrc;
    private final MetaData metaData;

    Package(Path packageRoot, Path packageSrc) {
        this.packageRoot = packageRoot;
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
            metaData = new MetaData(wrapper, packageRoot);
        } catch (NullPointerException e) {
            throw new PkgMetaException("Incomplete meta.json, not a valid PythonCraft package.", e);
        }
    }

    public Path getPackageRoot() {
        return packageRoot;
    }

    public Path getPackageSrc() {
        return packageSrc;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public String getFullName() {
        return String.format(
            "%s[%s]",
            metaData.getFriendlyName(),
            metaData.getIdentifier()
        );
    }

    @Override
    public String toString() {
        return "Package{" + metaData + '}';
    }
}
