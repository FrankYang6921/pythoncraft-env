package top.frankyang.pre.loader.exceptions;

import java.nio.file.Path;

public class PkgLoadException extends PackageException {
    public PkgLoadException() {
    }

    public PkgLoadException(String message) {
        super(message);
    }

    public PkgLoadException(String message, Path packageCause) {
        super(message, packageCause);
    }

    public PkgLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkgLoadException(String message, Throwable cause, Path packageCause) {
        super(message, cause, packageCause);
    }

    public PkgLoadException(Throwable cause) {
        super(cause);
    }

    public PkgLoadException(Throwable cause, Path packageCause) {
        super(cause, packageCause);
    }
}
