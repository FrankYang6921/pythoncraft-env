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

    private MetaData(Path packageSrc) {
        entrypoint =  thumbnail = null;
        identifier = description = null;
        friendlyName = packageSrc.getFileName().toString().replace(".disabled", "");  //q Don't show .disabled
        packageVersion = gameVersion = fabricVersion = loaderVersion = Version.ANY;
        classPaths = Collections.emptyList();
        assetPaths = Collections.emptyList();
    }

    public MetaData(MetaDataWrapper wrapper, Path packageRoot) {
        entrypoint = packageRoot.resolve(
            Objects.requireNonNull(wrapper.getEntrypointPath(), "Missing entrypointFilePath in metadata")
        );

        identifier =
            Objects.requireNonNull(wrapper.getIdentifier(), "Missing packageIdentifier in metadata.");

        String friendlyName = wrapper.getFriendlyName();
        this.friendlyName = friendlyName != null ? friendlyName : identifier;

        String description = wrapper.getDescription();
        this.description = description != null ? description : identifier;

        String thumbnailPath = wrapper.getThumbnailPath();
        this.thumbnail = thumbnailPath != null ? packageRoot.resolve(wrapper.getThumbnailPath()) : null;

        packageVersion = new Version(Objects.requireNonNull(
            wrapper.getPackageVersion(), "Missing packageVersion in metadata."
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

    static MetaData dummy(Path packageSrc) {
        return new MetaData(packageSrc) {
            @Override
            public Path getEntrypoint() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String getIdentifier() {
                return super.getFriendlyName();
            }

            @Override
            public String getFriendlyName() {
                return super.getFriendlyName();
            }

            @Override
            public String getDescription() {
                return super.getFriendlyName();
            }

            @Override
            public String getFullName() {
                return super.getFriendlyName();
            }
        };
    }

    public Path getEntrypoint() {
        return entrypoint;
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

    public Path getThumbnail() {
        return thumbnail;
    }

    public boolean hasThumbnail() {
        return thumbnail != null;
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

    public String getFullName() {
        return String.format(
            "%s[%s]",
            getFriendlyName(),
            getIdentifier()
        );
    }
}
