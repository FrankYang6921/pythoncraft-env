package top.frankyang.pre.packaging.exceptions;

public class PackageConstructionException extends MalformedPackageException {
    public PackageConstructionException() {
    }

    public PackageConstructionException(String message) {
        super(message);
    }

    public PackageConstructionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageConstructionException(Throwable cause) {
        super(cause);
    }
}
