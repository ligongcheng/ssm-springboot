package cn.it.ssm.common.ExceptionHandler;

public class ApiLimitedException extends AppException {

    public ApiLimitedException() {
    }

    public ApiLimitedException(String message) {
        super(message);
    }

    public ApiLimitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiLimitedException(Throwable cause) {
        super(cause);
    }

    public ApiLimitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
