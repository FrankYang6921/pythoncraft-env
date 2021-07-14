package top.frankyang.pre.loader.exceptions;

import java.nio.file.Path;

public class PkgMetaException extends PkgInitException {
    public PkgMetaException() {
    }

    public PkgMetaException(String message) {
        super(message);
    }

    public PkgMetaException(String message, Path packageCause) {
        super(message, packageCause);
    }

    public PkgMetaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkgMetaException(String message, Throwable cause, Path packageCause) {
        super(message, cause, packageCause);
    }

    public PkgMetaException(Throwable cause) {
        super(cause);
    }

    public PkgMetaException(Throwable cause, Path packageCause) {
        super(cause, packageCause);
    }
}
