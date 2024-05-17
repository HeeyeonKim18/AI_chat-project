package beside2.ten039.exception;

public class KakaoServerErrorException extends RuntimeException{
    public KakaoServerErrorException() {
        super();
    }

    public KakaoServerErrorException(String message) {
        super(message);
    }

    public KakaoServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public KakaoServerErrorException(Throwable cause) {
        super(cause);
    }

    protected KakaoServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
