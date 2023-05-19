package exceptions;

public class CoreValueException extends CoreException {

    public CoreValueException() {
        super();
    }

    public CoreValueException(String message) {
        super(message);
    }

    public CoreValueException(Throwable cause) {
        super(cause);
    }

    public CoreValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
