package top.frankyang.pre.loader.core;

import net.fabricmc.loader.util.version.SemanticVersionImpl;
import net.minecraft.util.Identifier;
import top.frankyang.pre.util.Versions;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class MetaDataPlaceholder implements MetaData {
    protected MetaDataPlaceholder() {
    }

    public static MetaDataPlaceholder getInstance() {
        return MetaDataPlaceholderSingleton.INSTANCE;
    }

    @Override
    public boolean hasThumbnail() {
        return false;
    }

    @Override
    public String getFullName() {
        return "不可用";
    }

    @Override
    public Path getEntrypoint() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "不可用";
    }

    @Override
    public String getFriendlyName() {
        return "不可用";
    }

    @Override
    public String getDescription() {
        return "不可用";
    }

    public Identifier getThumbnailId() {
        return new Identifier("pre:icon_false.png");
    }

    public Path getThumbnailPath() {
        return null;
    }

    @Override
    public SemanticVersionImpl getPackageVersion() {
        return Versions.ANY;
    }

    @Override
    public SemanticVersionImpl getGameVersion() {
        return Versions.ANY;
    }

    @Override
    public SemanticVersionImpl getFabricVersion() {
        return Versions.ANY;
    }

    @Override
    public SemanticVersionImpl getLoaderVersion() {
        return Versions.ANY;
    }

    @Override
    public List<Path> getClassPaths() {
        return Collections.emptyList();
    }

    @Override
    public List<Path> getAssetPaths() {
        return Collections.emptyList();
    }

    private static class MetaDataPlaceholderSingleton {
        private static final MetaDataPlaceholder INSTANCE = new MetaDataPlaceholder();
    }
}
