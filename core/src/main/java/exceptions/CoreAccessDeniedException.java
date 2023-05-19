package exceptions;

public class CoreAccessDeniedException extends CoreException {

    public CoreAccessDeniedException() {
        super();
    }

    public CoreAccessDeniedException(String message) {
        super(message);
    }

    public CoreAccessDeniedException(Throwable cause) {
        super(cause);
    }

    public CoreAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreAccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
