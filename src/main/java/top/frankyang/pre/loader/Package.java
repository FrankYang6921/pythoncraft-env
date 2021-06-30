package top.frankyang.pre.loader;

import top.frankyang.pre.util.JsonHelper;

import java.nio.file.Path;

public class Package {
    private final Path packageRoot;
    private final MetaData metaData;

    Package(Path packageRoot) {
        this.packageRoot = packageRoot;
        MetaDataWrapper wrapper = JsonHelper.deserialize(
            packageRoot.resolve("meta.json").toFile(), MetaDataWrapper.class
        );
        metaData = new MetaData(wrapper, packageRoot);
    }

    public Path getPackageRoot() {
        return packageRoot;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public String toString() {
        return "Package{" +
            "metaData=" + metaData +
            '}';
    }
}
