package top.frankyang.pre.loader.core;

import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.util.Packages;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class PackageInfo {
    private final String fullName;
    private final String description;
    private final boolean disabled;
    private final Path packageSrc;
    private final ImageIcon thumbnail;

    public PackageInfo(Package pkg) {
        fullName = pkg
            .getMetaData()
            .getFullName();
        description = pkg
            .getMetaData()
            .getDescription();
        disabled = pkg.isDummy();
        packageSrc = pkg.getPackageSrc();

        MetaData meta = pkg.getMetaData();
        if (meta.hasThumbnail() && Files.exists(meta.getThumbnail()) && meta.getThumbnail().toString().endsWith(".png")) {
            thumbnail = new ImageIcon(meta.getThumbnail().toString());
        } else if (disabled) {
            PythonCraft.getInstance().getLogger().warn("Package " + pkg + "(disabled) does not have a thumbnail.");
            thumbnail = new ImageIcon(Objects.requireNonNull(PackageInfo.class.getResource("/assets/pre/icon_false.png")));
        } else {
            PythonCraft.getInstance().getLogger().warn("Package " + pkg + "(enabled) does not have a thumbnail.");
            thumbnail = new ImageIcon(Objects.requireNonNull(PackageInfo.class.getResource("/assets/pre/icon_white.png")));
        }
    }

    public PackageInfo(String fullName, String description, boolean disabled, Path packageSrc, ImageIcon thumbnail) {
        this.fullName = fullName;
        this.description = description;
        this.disabled = disabled;
        this.packageSrc = packageSrc;
        this.thumbnail = thumbnail;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public File getPackageDir() {
        return packageSrc.getParent().toFile();
    }

    public String getFileName() {
        return packageSrc.getFileName().toString();
    }

    public ImageIcon getThumbnail() {
        return thumbnail;
    }

    public PackageInfo disable() {
        if (disabled)
            throw new UnsupportedOperationException();
        Path src = Packages.disable(packageSrc);
        return new PackageInfo(fullName, description, true, src, new ImageIcon(Objects.requireNonNull(PackageInfo.class.getResource("/assets/pre/icon_false.png"))));
    }

    public PackageInfo enable() {
        if (!disabled)
            throw new UnsupportedOperationException();
        Path src = Packages.enable(packageSrc);
        return new PackageInfo(fullName, description, false, src, new ImageIcon(Objects.requireNonNull(PackageInfo.class.getResource("/assets/pre/icon_white.png"))));
    }
}
