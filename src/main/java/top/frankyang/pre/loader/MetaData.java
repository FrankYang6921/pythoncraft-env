package top.frankyang.pre.loader;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MetaData {
    private final Path entrypointFilePath;
    private final String packageIdentifier;
    private final String packageFriendlyName;
    private final String packageDescription;
    private final Path packageThumbnailPath;
    private final List<Path> assetProviderPaths;
    private final List<Path> classProviderPaths;

    private final Path packageRoot;

    public MetaData(MetaDataWrapper wrapper, Path packageRoot) {
        this.packageRoot = packageRoot;

        entrypointFilePath = packageRoot.resolve(
            Objects.requireNonNull(wrapper.getEntrypointFilePath(), "元数据中缺少模组入口（entrypointFilePath）。")
        );
        packageIdentifier =
            Objects.requireNonNull(wrapper.getPackageIdentifier(), "元数据中缺少模组标识符（packageIdentifier）。");
        String friendlyName = wrapper.getPackageFriendlyName();
        packageFriendlyName = friendlyName != null ? friendlyName : "<N/A>";
        String description = wrapper.getPackageDescription();
        packageDescription = description != null ? description : "<N/A>";
        String thumbnailPath = wrapper.getPackageThumbnailPath();
        packageThumbnailPath = thumbnailPath != null ? packageRoot.resolve(wrapper.getPackageThumbnailPath()) : null;
        assetProviderPaths = Arrays.stream(wrapper.getAssetProviderPaths())
            .map(packageRoot::resolve)
            .collect(Collectors.toList());
        classProviderPaths = Arrays.stream(wrapper.getClassProviderPaths())
            .map(packageRoot::resolve)
            .collect(Collectors.toList());
    }

    public Path getEntrypointFilePath() {
        return entrypointFilePath;
    }

    public String getPackageIdentifier() {
        return packageIdentifier;
    }

    public String getPackageFriendlyName() {
        return packageFriendlyName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public Path getPackageThumbnailPath() {
        return packageThumbnailPath;
    }

    public boolean hasPackageThumbnail() {
        return packageThumbnailPath != null;
    }

    public List<Path> getAssetProviderPaths() {
        return assetProviderPaths;
    }

    public List<Path> getClassProviderPaths() {
        return classProviderPaths;
    }

    @Override
    public String toString() {
        return "MetaData{" +
            "entrypointFilePath=" + entrypointFilePath +
            ", packageIdentifier='" + packageIdentifier + '\'' +
            ", packageFriendlyName='" + packageFriendlyName + '\'' +
            ", packageDescription='" + packageDescription + '\'' +
            ", packageThumbnailPath=" + packageThumbnailPath +
            ", assetProviderPaths=" + assetProviderPaths +
            ", classProviderPaths=" + classProviderPaths +
            ", packageRoot=" + packageRoot +
            '}';
    }
}
