package cn.it.ssm.common.ExceptionHandler;

public class AccountChangeException extends AppException {
    public AccountChangeException(String message) {
        super(message);
    }

    public AccountChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
