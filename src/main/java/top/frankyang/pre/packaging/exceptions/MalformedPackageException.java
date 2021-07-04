package top.frankyang.pre.packaging.exceptions;

public class MalformedPackageException extends RuntimeException {
    public MalformedPackageException() {
    }

    public MalformedPackageException(String message) {
        super(message);
    }

    public MalformedPackageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedPackageException(Throwable cause) {
        super(cause);
    }
}
