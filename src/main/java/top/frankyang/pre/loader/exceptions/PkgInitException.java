package top.frankyang.pre.loader.exceptions;

import java.nio.file.Path;

public class PkgInitException extends PackageException {
    public PkgInitException() {
    }

    public PkgInitException(String message) {
        super(message);
    }

    public PkgInitException(String message, Path packageCause) {
        super(message, packageCause);
    }

    public PkgInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkgInitException(String message, Throwable cause, Path packageCause) {
        super(message, cause, packageCause);
    }

    public PkgInitException(Throwable cause) {
        super(cause);
    }

    public PkgInitException(Throwable cause, Path packageCause) {
        super(cause, packageCause);
    }
}
