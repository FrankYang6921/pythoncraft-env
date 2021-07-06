package top.frankyang.pre.loader.exceptions;

public class PackageException extends RuntimeException {
    public PackageException() {
    }

    public PackageException(String message) {
        super(message);
    }

    public PackageException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageException(Throwable cause) {
        super(cause);
    }
}
