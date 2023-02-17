package world.hzq.linkcode.util;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 17:26
 */
public enum ResultState {
    SUCCESS(200), //200:响应成功
    FAIL(502),    //502:请求失败,错误原因未知
    FALSE(400),   //400:请求失败,请求参数错误
    ERROR(500);   //500:响应失败,服务器内部异常
    private Integer code;

    private ResultState(Integer code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
