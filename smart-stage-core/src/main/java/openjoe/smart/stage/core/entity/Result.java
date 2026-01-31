package openjoe.smart.stage.core.entity;

import openjoe.smart.stage.core.enums.ErrorCodeEnum;

import java.beans.Transient;

/**
 * 响应结果
 */
public class Result<T> {

	/**
	 * 响应码
	 */
	private String code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 数据
	 */
	private T data;

    public Result() {
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

	public static <T> Result<T> success() {
        return new Result<>(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMessage());
	}

	public static <T> Result<T> success(T data) {
		Result<T> r = success();
		r.setData(data);
		return r;
	}

    public static <T> Result<T> error(String code, String message) {
        return new Result<>(code, message);
    }

	public String getCode() {
		return code;
	}

	public Result<T> setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Result<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

	@Transient
	public boolean isSuccess() {
		return ErrorCodeEnum.SUCCESS.getCode().equals(this.code);
	}

	@Override
	public String toString() {
		return "Result{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}