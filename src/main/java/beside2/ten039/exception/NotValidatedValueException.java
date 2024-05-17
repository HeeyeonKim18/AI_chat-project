package beside2.ten039.exception;

public class NotValidatedValueException extends RuntimeException{
    public NotValidatedValueException() {
        super();
    }

    public NotValidatedValueException(String message) {
        super(message);
    }

    public NotValidatedValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidatedValueException(Throwable cause) {
        super(cause);
    }

    protected NotValidatedValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
