package top.frankyang.pre.packaging.unpacking;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class MetaDataWrapper {
    @SerializedName(value = "entrypointPath", alternate = {"entryPnt"})
    private final String entrypointPath;
    @SerializedName(value = "identifier", alternate = {"id"})
    private final String identifier;
    @SerializedName(value = "friendlyName", alternate = {"name"})
    private final String friendlyName;
    @SerializedName(value = "description", alternate = {"info"})
    private final String description;
    @SerializedName(value = "thumbnailPath", alternate = {"img"})
    private final String thumbnailPath;
    @SerializedName(value = "packageVersion", alternate = {"pkgVer"})
    private final String packageVersion;
    @SerializedName(value = "gameVersion", alternate = {"gameVer"})
    private final String gameVersion;
    @SerializedName(value = "fabricVersion", alternate = {"fabricVer"})
    private final String fabricVersion;
    @SerializedName(value = "loaderVersion", alternate = {"loaderVer"})
    private final String loaderVersion;
    @SerializedName(value = "assetProviders", alternate = {"assets"})
    private final String[] assetProviders;
    @SerializedName(value = "classProviders", alternate = {"classes"})
    private final String[] classProviders;

    private MetaDataWrapper(String entrypointPath,
                            String identifier,
                            String friendlyName,
                            String description,
                            String thumbnailPath,
                            String packageVersion,
                            String gameVersion,
                            String fabricVersion,
                            String loaderVersion,
                            String[] assetProviders,
                            String[] classProviders) {
        this.entrypointPath = entrypointPath;
        this.identifier = identifier;
        this.friendlyName = friendlyName;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.packageVersion = packageVersion;
        this.gameVersion = gameVersion;
        this.fabricVersion = fabricVersion;
        this.loaderVersion = loaderVersion;
        this.assetProviders = assetProviders;
        this.classProviders = classProviders;
    }


    public String getEntrypointPath() {
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

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getFabricVersion() {
        return fabricVersion;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }

    public String[] getAssetProviders() {
        return assetProviders;
    }

    public String[] getClassProviders() {
        return classProviders;
    }

    @Override
    public String toString() {
        return "MetaDataWrapper{" +
            "entrypointPath='" + entrypointPath + '\'' +
            ", identifier='" + identifier + '\'' +
            ", friendlyName='" + friendlyName + '\'' +
            ", description='" + description + '\'' +
            ", thumbnailPath='" + thumbnailPath + '\'' +
            ", packageVersion='" + packageVersion + '\'' +
            ", gameVersion='" + gameVersion + '\'' +
            ", fabricVersion='" + fabricVersion + '\'' +
            ", loaderVersion='" + loaderVersion + '\'' +
            ", assetProviders=" + Arrays.toString(assetProviders) +
            ", classProviders=" + Arrays.toString(classProviders) +
            '}';
    }
}
