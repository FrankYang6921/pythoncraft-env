package top.frankyang.pre.loader.loader;

import top.frankyang.pre.misc.Version;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MetaData {
    private final Path entrypointPath;
    private final String identifier;
    private final String friendlyName;
    private final String description;
    private final Path thumbnailPath;
    private final Version packageVersion;
    private final Version gameVersion;
    private final Version fabricVersion;
    private final Version loaderVersion;
    private final List<Path> classPaths;
    private final List<Path> assetPaths;

    public MetaData(MetaDataWrapper wrapper, Path packageRoot) {
        entrypointPath = packageRoot.resolve(
            Objects.requireNonNull(wrapper.getEntrypointPath(), "元数据中缺少模组入口（entrypointFilePath）。")
        );

        identifier =
            Objects.requireNonNull(wrapper.getIdentifier(), "元数据中缺少模组标识符（packageIdentifier）。");

        String friendlyName = wrapper.getFriendlyName();
        this.friendlyName = friendlyName != null ? friendlyName : "<N/A>";

        String description = wrapper.getDescription();
        this.description = description != null ? description : "<N/A>";

        String thumbnailPath = wrapper.getThumbnailPath();
        this.thumbnailPath = thumbnailPath != null ? packageRoot.resolve(wrapper.getThumbnailPath()) : null;

        packageVersion = new Version(Objects.requireNonNull(
            wrapper.getPackageVersion(), "元数据中缺少模组版本号（packageVersion）。"
        ));

        String version = wrapper.getGameVersion();
        gameVersion = version != null ? new Version(version) : Version.ANY;

        version = wrapper.getFabricVersion();
        fabricVersion = version != null ? new Version(version) : Version.ANY;

        version = wrapper.getLoaderVersion();
        loaderVersion = version != null ? new Version(version) : Version.ANY;

        if (wrapper.getClassPaths() != null)
            classPaths = Arrays.stream(wrapper.getClassPaths())
                .map(packageRoot::resolve)
                .collect(Collectors.toList());
        else
            classPaths = Collections.emptyList();

        if (wrapper.getAssetPaths() != null)
            assetPaths = Arrays.stream(wrapper.getAssetPaths())
                .map(packageRoot::resolve)
                .collect(Collectors.toList());
        else
            assetPaths = Collections.emptyList();
    }

    public Path getEntrypointPath() {
        return entrypointPath;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getDescription() {
        return description;
    }

    public Path getThumbnailPath() {
        return thumbnailPath;
    }

    public boolean hasPackageThumbnail() {
        return thumbnailPath != null;
    }

    public Version getPackageVersion() {
        return packageVersion;
    }

    public Version getGameVersion() {
        return gameVersion;
    }

    public Version getFabricVersion() {
        return fabricVersion;
    }

    public Version getLoaderVersion() {
        return loaderVersion;
    }

    public List<Path> getClassPaths() {
        return classPaths;
    }

    public List<Path> getAssetPaths() {
        return assetPaths;
    }
}
