package top.frankyang.pre.loader.core;

import net.fabricmc.loader.util.version.SemanticVersionImpl;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.List;

public interface MetaData {
    boolean hasThumbnail();

    String getFullName();

    Path getEntrypoint();

    String getIdentifier();

    String getFriendlyName();

    String getDescription();

    Identifier getThumbnailId();

    Path getThumbnailPath();

    SemanticVersionImpl getPackageVersion();

    SemanticVersionImpl getGameVersion();

    SemanticVersionImpl getFabricVersion();

    SemanticVersionImpl getLoaderVersion();

    List<Path> getClassPaths();

    List<Path> getAssetPaths();
}
