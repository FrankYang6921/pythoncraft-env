package top.frankyang.pre.packaging.exceptions;

public class PackageInitializationException extends MalformedPackageException {
    public PackageInitializationException() {
    }

    public PackageInitializationException(String message) {
        super(message);
    }

    public PackageInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageInitializationException(Throwable cause) {
        super(cause);
    }
}
