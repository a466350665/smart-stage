package openjoe.smart.stage.core.enums;

/**
 * @author Joe
 */
public enum ErrorCodeEnum implements ErrorCode {

    SUCCESS("000000", "成功"),
    ERROR("000001", "未知错误"),
    VALIDATION_ERROR("000002", ""),;

    private String code;
    private String desc;

    ErrorCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}