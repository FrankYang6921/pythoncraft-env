package top.frankyang.pre.loader.exceptions;

public class PkgLoadException extends PackageException {
    public PkgLoadException() {
    }

    public PkgLoadException(String message) {
        super(message);
    }

    public PkgLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkgLoadException(Throwable cause) {
        super(cause);
    }
}
