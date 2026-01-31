package openjoe.smart.stage.exception;

import openjoe.smart.stage.core.enums.ErrorCodeEnum;

/**
 * 异常基类
 */
public class CommonException extends RuntimeException {

    private String code;

    public CommonException(String message) {
        super(message);
        this.code = ErrorCodeEnum.ERROR.getCode();
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(String code, Throwable cause, String message) {
        super(message, cause);
        this.code = code;
    }

    public CommonException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}