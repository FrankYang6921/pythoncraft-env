package top.frankyang.pre.loader.core;

import top.frankyang.pre.misc.Version;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MetaData {
    private final Path entrypoint;
    private final String identifier;
    private final String friendlyName;
    private final String description;
    private final Path thumbnail;
    private final Version packageVersion;
    private final Version gameVersion;
    private final Version fabricVersion;
    private final Version loaderVersion;
    private final List<Path> classPaths;
    private final List<Path> assetPaths;

    MetaData(Path packageSrc) {
        entrypoint = thumbnail = null;
        identifier = description = friendlyName = packageSrc.getFileName().toString().replace(".disabled", "");  //q Don't show .disabled
        packageVersion = gameVersion = fabricVersion = loaderVersion = Version.ANY;
        classPaths = Collections.emptyList();
        assetPaths = Collections.emptyList();
    }

    public MetaData(MetaDataWrapper wrapper, Path packageRoot) {
        entrypoint = packageRoot.resolve(
            Objects.requireNonNull(wrapper.getEntrypoint(), "Missing key: 'entrypoint' in metadata")
        );

        identifier =
            Objects.requireNonNull(wrapper.getIdentifier(), "Missing key: 'identifier' in metadata.");

        String friendlyName = wrapper.getFriendlyName();
        this.friendlyName = friendlyName != null ? friendlyName : identifier;

        String description = wrapper.getDescription();
        this.description = description != null ? description : identifier;

        String thumbnailPath = wrapper.getThumbnailPath();
        this.thumbnail = thumbnailPath != null ? packageRoot.resolve(wrapper.getThumbnailPath()) : null;

        packageVersion = new Version(Objects.requireNonNull(
            wrapper.getPackageVersion(), "Missing key: 'packageVersion' in metadata."
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

    public boolean hasThumbnail() {
        return thumbnail != null;
    }

    public String getFullName() {
        return String.format(
            "%s[%s]",
            getFriendlyName(),
            getIdentifier()
        );
    }

    public Path getEntrypoint() {
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

    public Path getThumbnail() {
        return this.thumbnail;
    }

    public Version getPackageVersion() {
        return this.packageVersion;
    }

    public Version getGameVersion() {
        return this.gameVersion;
    }

    public Version getFabricVersion() {
        return this.fabricVersion;
    }

    public Version getLoaderVersion() {
        return this.loaderVersion;
    }

    public List<Path> getClassPaths() {
        return this.classPaths;
    }

    public List<Path> getAssetPaths() {
        return this.assetPaths;
    }
}
