package top.frankyang.pre.packaging.unpacking;

import com.google.gson.JsonParseException;
import top.frankyang.pre.packaging.exceptions.PackageMetaDataException;
import top.frankyang.pre.util.JsonHelper;

import java.io.IOException;
import java.nio.file.Path;

public class Package {
    private final Path packageRoot;
    private final MetaData metaData;

    Package(Path packageRoot) {
        this.packageRoot = packageRoot;
        MetaDataWrapper wrapper;
        try {
            wrapper = JsonHelper.deserialize(
                packageRoot.resolve("meta.json").toFile(), MetaDataWrapper.class
            );
        } catch (IOException e) {
            throw new PackageMetaDataException("无法找到meta.json，不是有效的PythonCraft包。", e);
        } catch (JsonParseException e) {
            throw new PackageMetaDataException("无法解析meta.json，不是有效的PythonCraft包。", e);
        }

        try {
            metaData = new MetaData(wrapper, packageRoot);
        } catch (NullPointerException e) {
            throw new PackageMetaDataException("不完整的meta.json，不是有效的PythonCraft包。", e);
        }
    }

    public Path getPackageRoot() {
        return packageRoot;
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
