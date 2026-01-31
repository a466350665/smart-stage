package openjoe.smart.stage.exception;

import openjoe.smart.stage.core.enums.ErrorCode;

/**
 * 应用服务异常
 */
public class ApplicationException extends CommonException {

    public ApplicationException(ErrorCode ec) {
        super(ec.getCode(), ec.getMessage());
    }

    public ApplicationException(ErrorCode ec, Object... args) {
        super(ec.getCode(), ec.getMessage(args));
    }

    public ApplicationException(ErrorCode ec, Throwable cause) {
        super(ec.getCode(), cause, ec.getMessage());
    }

    public ApplicationException(ErrorCode ec, Throwable cause, Object... args) {
        super(ec.getCode(), cause, ec.getMessage(args));
    }
}