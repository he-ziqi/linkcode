package world.hzq.linkcode.util;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 17:25
 */
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Result() {
    }

    private Result(Integer code) {
        this.code = code;
    }

    private Result(String msg) {
        this.msg = msg;
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    private Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(ResultState.SUCCESS.getCode(), msg, data);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(ResultState.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultState.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultState.SUCCESS.getCode());
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(ResultState.FAIL.getCode(), msg);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultState.FAIL.getCode());
    }

    public static <T> Result<T> badRequest(String msg) {
        return new Result<>(ResultState.FALSE.getCode(), msg);
    }

    public static <T> Result<T> badRequest() {
        return new Result<>(ResultState.FALSE.getCode());
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(ResultState.ERROR.getCode(), msg);
    }

    public static <T> Result<T> error() {
        return new Result<>(ResultState.ERROR.getCode());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}