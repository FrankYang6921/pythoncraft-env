package top.frankyang.pre.util;

import top.frankyang.pre.loader.exceptions.RuntimeIOException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Packages {
    private Packages() {
    }

    public static Path disable(Path src) {
        try {
            return Files.move(
                src, src.resolveSibling(
                    src.getFileName().toString().concat(".disabled")
                )
            );
        } catch (IOException e) {
            throw new RuntimeIOException("Cannot disable a certain package.", e);
        }
    }

    public static Path enable(Path src) {
        String i = src.getFileName().toString();
        try {
            return Files.move(
                src, src.resolveSibling(i.substring(0, i.length() - 9))
            );
        } catch (IOException e) {
            throw new RuntimeIOException("Cannot enable a certain package.", e);
        }
    }
}
