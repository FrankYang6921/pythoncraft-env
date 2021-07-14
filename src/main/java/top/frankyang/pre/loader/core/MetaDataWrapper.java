package top.frankyang.pre.loader.core;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName(value = "classPaths", alternate = {"classes"})
    private final String[] classPaths;
    @SerializedName(value = "assetPaths", alternate = {"assets"})
    private final String[] assetPaths;

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
                            String[] classPaths,
                            String[] assetPaths) {
        this.entrypointPath = entrypointPath;
        this.identifier = identifier;
        this.friendlyName = friendlyName;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.packageVersion = packageVersion;
        this.gameVersion = gameVersion;
        this.fabricVersion = fabricVersion;
        this.loaderVersion = loaderVersion;
        this.classPaths = classPaths;
        this.assetPaths = assetPaths;
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

    public String[] getClassPaths() {
        return classPaths;
    }

    public String[] getAssetPaths() {
        return assetPaths;
    }
}
