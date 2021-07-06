package top.frankyang.pre.loader.exceptions;

public class PkgMetaException extends PkgInitException {
    public PkgMetaException() {
    }

    public PkgMetaException(String message) {
        super(message);
    }

    public PkgMetaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkgMetaException(Throwable cause) {
        super(cause);
    }
}
