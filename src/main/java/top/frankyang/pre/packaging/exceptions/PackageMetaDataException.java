package top.frankyang.pre.packaging.exceptions;

public class PackageMetaDataException extends PackageConstructionException {
    public PackageMetaDataException() {
    }

    public PackageMetaDataException(String message) {
        super(message);
    }

    public PackageMetaDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageMetaDataException(Throwable cause) {
        super(cause);
    }
}
