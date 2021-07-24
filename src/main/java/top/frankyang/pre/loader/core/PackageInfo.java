package top.frankyang.pre.loader.core;

import net.minecraft.util.Identifier;
import top.frankyang.pre.util.Packages;

import java.io.File;
import java.nio.file.Path;

public class PackageInfo {
    private final String nameString;
    private final String infoString;
    private final boolean disabled;
    private final Path packageSrc;
    private final Identifier thumbnail;
    private final MetaData metaData;
    private final boolean placeholder;

    public PackageInfo(Package pkg) {
        metaData = pkg.getMetaData();
        nameString = metaData.getFriendlyName();
        infoString = metaData.getDescription();
        disabled = pkg.isPlaceholder();
        packageSrc = pkg.getPackageSrc();
        thumbnail = metaData.getThumbnailId();
        placeholder = pkg.isPlaceholder();
    }

    public PackageInfo(boolean disabled, Path packageSrc, MetaData metaData) {
        this.nameString = metaData.getFriendlyName();
        this.infoString = metaData.getDescription();
        this.disabled = disabled;
        this.packageSrc = packageSrc;
        this.thumbnail = metaData.getThumbnailId();
        this.metaData = metaData;
        placeholder = true;
    }

    public PackageInfo(String nameString,
                       String infoString,
                       boolean disabled,
                       Path packageSrc,
                       Identifier thumbnail,
                       MetaData metaData) {
        this.nameString = nameString;
        this.infoString = infoString;
        this.disabled = disabled;
        this.packageSrc = packageSrc;
        this.thumbnail = thumbnail;
        this.metaData = metaData;
        placeholder = true;
    }

    public String getNameString() {
        return nameString;
    }

    public String getInfoString() {
        return infoString;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }

    public Path getPackageSrc() {
        return packageSrc;
    }

    public File getPackageDir() {
        return packageSrc.getParent().toFile();
    }

    public String getFileName() {
        return packageSrc.getFileName().toString();
    }

    public Identifier getThumbnail() {
        return thumbnail;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public PackageInfo disable() {
        if (disabled)
            throw new UnsupportedOperationException();
        Path src = Packages.disable(packageSrc);
        return new PackageInfo(true, src, MetaDataPlaceholder.getInstance());
    }

    public PackageInfo enable() {
        if (!disabled)
            throw new UnsupportedOperationException();
        Path src = Packages.enable(packageSrc);
        return new PackageInfo(false, src, MetaDataPlaceholder.getInstance());
    }
}
