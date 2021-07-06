package top.frankyang.pre.loader.exceptions;

public class PkgInitException extends PackageException {
    public PkgInitException() {
    }

    public PkgInitException(String message) {
        super(message);
    }

    public PkgInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkgInitException(Throwable cause) {
        super(cause);
    }
}
