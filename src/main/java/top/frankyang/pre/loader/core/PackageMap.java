package top.frankyang.pre.loader.core;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class PackageMap implements Iterable<Package> {
    private final HashMap<String, Package> filenameToPkg = new HashMap<>();
    private final HashMap<String, Package> identifierToPkg = new HashMap<>();

    public void put(Package pkg) {
        String filename = pkg.getPackageSrc().getFileName().toString();
        if (filenameToPkg.containsKey(filename)) {
            throw new UnsupportedOperationException("Duplicated filename: " + filename);
        }
        String identifier = pkg.getMetaData().getIdentifier();
        if (identifierToPkg.containsKey(identifier)) {
            throw new UnsupportedOperationException("Duplicated identifier: " + identifier);
        }
        filenameToPkg.put(filename, pkg);
        identifierToPkg.put(filename, pkg);
    }

    public Package getByFilename(String filename) {
        return getByFilename(Paths.get(filename));
    }

    public Package getByFilename(Path filename) {
        return Objects.requireNonNull(
            filenameToPkg.get(filename.getFileName().toString()),
            "Package filename not found: " + filename
        );
    }

    public Package getByIdentifier(String identifier) {
        return Objects.requireNonNull(
            identifierToPkg.get(identifier),
            "Package identifier not found: " + identifier
        );
    }

    public Collection<Package> packages() {
        return identifierToPkg.values();
    }

    public Stream<Package> stream() {
        return identifierToPkg.values().stream();
    }

    @NotNull
    @Override
    public Iterator<Package> iterator() {
        return identifierToPkg.values().iterator();
    }
}
