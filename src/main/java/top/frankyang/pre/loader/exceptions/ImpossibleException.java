package top.frankyang.pre.loader.exceptions;

public class ImpossibleException extends RuntimeException {
    public ImpossibleException() {
    }

    public ImpossibleException(String message) {
        super(message);
    }

    public ImpossibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImpossibleException(Throwable cause) {
        super(cause);
    }
}
