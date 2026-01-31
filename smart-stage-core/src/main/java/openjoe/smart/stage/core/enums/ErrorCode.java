package openjoe.smart.stage.core.enums;

import openjoe.smart.stage.core.util.MessageUtils;

/**
 * 错误码定义接口
 *
 * @author Joe
 */
public interface ErrorCode {

    /**
     * 错误码
     */
    String getCode();

    /**
     * 错误码描述
     */
    String getDesc();

    default String getMessage(Object... args) {
        return MessageUtils.getOrDefault(getCode(), getDesc(), args);
    }
}