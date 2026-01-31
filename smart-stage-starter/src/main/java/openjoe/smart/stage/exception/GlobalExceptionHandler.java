package openjoe.smart.stage.exception;

import openjoe.smart.stage.core.entity.Result;
import openjoe.smart.stage.core.enums.ErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 公共异常拦截处理
 */
@Order(100)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public Object handleException(CommonException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("global exception.", e);
        return Result.error(ErrorCodeEnum.ERROR.getCode(), ErrorCodeEnum.ERROR.getMessage());
    }

    /**
     * 请求参数缺失异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Object handleException(MissingServletRequestParameterException e) {
        log.error("parameter exception.", e);
        return Result.error(ErrorCodeEnum.VALIDATION_ERROR.getCode(), e.getMessage());
    }

    /**
     * 请求参数类型异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Object handleException(MethodArgumentTypeMismatchException e) {
        log.error("parameter exception.", e);
        return Result.error(ErrorCodeEnum.VALIDATION_ERROR.getCode(), e.getMessage());
    }

    /**
     * 数据校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleException(MethodArgumentNotValidException e) {
        List<FieldError> errorList = e.getBindingResult().getFieldErrors();
        String message = errorList.stream().map(t -> t.getField() + ":" + t.getDefaultMessage()).collect(Collectors.joining(";"));
        return Result.error(ErrorCodeEnum.VALIDATION_ERROR.getCode(), message);
    }
}