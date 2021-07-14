package top.frankyang.pre.loader.exceptions;

import java.nio.file.Path;

public class PackageException extends RuntimeException {
    private final Path causeSrc;

    public PackageException() {
        causeSrc = null;
    }

    public PackageException(String message) {
        this(message, (Path) null);
    }

    public PackageException(String message, Path causeSrc) {
        super(message);
        this.causeSrc = causeSrc;
    }

    public PackageException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public PackageException(String message, Throwable cause, Path causeSrc) {
        super(message, cause);
        this.causeSrc = causeSrc;
    }

    public PackageException(Throwable cause) {
        this(cause, null);
    }

    public PackageException(Throwable cause, Path causeSrc) {
        super(cause);
        this.causeSrc = causeSrc;
    }

    public Path getCauseSrc() {
        return causeSrc;
    }
}
