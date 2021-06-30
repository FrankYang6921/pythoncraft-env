package top.frankyang.pre.loader;

import java.util.Arrays;

public class MetaDataWrapper {
    private final String entrypointFilePath;
    private final String packageIdentifier;
    private final String packageFriendlyName;
    private final String packageDescription;
    private final String packageThumbnailPath;
    private final String[] assetProviderPaths;
    private final String[] classProviderPaths;

    private MetaDataWrapper(String entrypointFilePath,
                            String packageIdentifier,
                            String packageFriendlyName,
                            String packageDescription,
                            String packageThumbnailPath,
                            String[] assetProviderPaths,
                            String[] classProviderPaths) {
        this.entrypointFilePath = entrypointFilePath;
        this.packageIdentifier = packageIdentifier;
        this.packageFriendlyName = packageFriendlyName;
        this.packageDescription = packageDescription;
        this.packageThumbnailPath = packageThumbnailPath;
        this.assetProviderPaths = assetProviderPaths;
        this.classProviderPaths = classProviderPaths;
    }


    public String getEntrypointFilePath() {
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

    public String getPackageThumbnailPath() {
        return packageThumbnailPath;
    }

    public String[] getAssetProviderPaths() {
        return assetProviderPaths;
    }

    public String[] getClassProviderPaths() {
        return classProviderPaths;
    }

    @Override
    public String toString() {
        return "PackageMetaData{" +
            "entrypointFilePath='" + entrypointFilePath + '\'' +
            ", packageIdentifier='" + packageIdentifier + '\'' +
            ", packageDescription='" + packageDescription + '\'' +
            ", packageThumbnailPath='" + packageThumbnailPath + '\'' +
            ", assetProviderPaths=" + Arrays.toString(assetProviderPaths) +
            ", classProviderPaths=" + Arrays.toString(classProviderPaths) +
            '}';
    }
}
