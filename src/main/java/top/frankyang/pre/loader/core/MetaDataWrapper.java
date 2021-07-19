package top.frankyang.pre.loader.core;

import com.google.gson.annotations.SerializedName;

public class MetaDataWrapper {
    @SerializedName(value = "entrypoint", alternate = {"main"})
    private final String entrypoint;
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

    public MetaDataWrapper(String entrypoint,
                           String identifier,
                           String friendlyName,
                           String description,
                           String thumbnailPath,
                           String packageVersion,
                           String gameVersion,
                           String fabricVersion,
                           String loaderVersion,
                           String[] classPaths,
                           String[] assetPaths) {
        this.entrypoint = entrypoint;
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

    public String getEntrypoint() {
        return this.entrypoint;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getThumbnailPath() {
        return this.thumbnailPath;
    }

    public String getPackageVersion() {
        return this.packageVersion;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public String getFabricVersion() {
        return this.fabricVersion;
    }

    public String getLoaderVersion() {
        return this.loaderVersion;
    }

    public String[] getClassPaths() {
        return this.classPaths;
    }

    public String[] getAssetPaths() {
        return this.assetPaths;
    }
}
