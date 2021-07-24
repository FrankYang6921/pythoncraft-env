package top.frankyang.pre.loader.core;

import net.fabricmc.loader.util.version.SemanticVersionImpl;
import net.minecraft.util.Identifier;
import top.frankyang.pre.util.Versions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MetaDataImpl implements MetaData {
    private final Path entrypoint;
    private final String identifier;
    private final String friendlyName;
    private final String description;
    private final Path thumbnail;
    private final SemanticVersionImpl packageVersion;
    private final SemanticVersionImpl gameVersion;
    private final SemanticVersionImpl fabricVersion;
    private final SemanticVersionImpl loaderVersion;
    private final List<Path> classPaths;
    private final List<Path> assetPaths;

    public MetaDataImpl(MetaDataWrapper wrapper, Path packageRoot) {
        entrypoint = packageRoot.resolve(
            Objects.requireNonNull(wrapper.getEntrypoint(), "Missing key: 'entrypoint' in metadata.")
        );

        identifier =
            Objects.requireNonNull(wrapper.getIdentifier(), "Missing key: 'identifier' in metadata.");

        String friendlyName = wrapper.getFriendlyName();
        this.friendlyName = friendlyName != null ? friendlyName : identifier;

        String description = wrapper.getDescription();
        this.description = description != null ? description : identifier;

        String thumbnailPath = wrapper.getThumbnailPath();
        this.thumbnail = thumbnailPath != null ? packageRoot.resolve(wrapper.getThumbnailPath()) : null;

        packageVersion = Versions.of(Objects.requireNonNull(
            wrapper.getPackageVersion(), "Missing key: 'packageVersion' in metadata."
        ));

        String version = wrapper.getGameVersion();
        gameVersion = Versions.ofNullable(version);

        version = wrapper.getFabricVersion();
        fabricVersion = Versions.ofNullable(version);

        version = wrapper.getLoaderVersion();
        loaderVersion = Versions.ofNullable(version);

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

    @Override
    public boolean hasThumbnail() {
        return thumbnail != null;
    }

    @Override
    public String getFullName() {
        return String.format(
            "%s[%s]",
            getFriendlyName(),
            getIdentifier()
        );
    }

    @Override
    public Path getEntrypoint() {
        return this.entrypoint;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getFriendlyName() {
        return this.friendlyName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public Identifier getThumbnailId() {
        if (hasThumbnail())
            return new Identifier(getIdentifier() + ":thumbnail.png");
        return new Identifier("pre:icon_white.png");
    }

    public Path getThumbnailPath() {
        return thumbnail;
    }

    @Override
    public SemanticVersionImpl getPackageVersion() {
        return this.packageVersion;
    }

    @Override
    public SemanticVersionImpl getGameVersion() {
        return this.gameVersion;
    }

    @Override
    public SemanticVersionImpl getFabricVersion() {
        return this.fabricVersion;
    }

    @Override
    public SemanticVersionImpl getLoaderVersion() {
        return this.loaderVersion;
    }

    @Override
    public List<Path> getClassPaths() {
        return this.classPaths;
    }

    @Override
    public List<Path> getAssetPaths() {
        return this.assetPaths;
    }
}
