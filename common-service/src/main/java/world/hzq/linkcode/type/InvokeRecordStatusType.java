package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 执行记录状态类型
 * @date 2023/2/9 23:34
 */
public enum InvokeRecordStatusType {
    INVOKE_EXCEPTION_TYPE("exception","执行异常"),
    INVOKE_PASS_TYPE("pass","执行通过"),
    INVOKE_NOT_PASS_TYPE("not_pass","执行正常,但未通过"),
    INVOKE_TIME_LIMIT_TYPE("time_limit","执行超时"),
    INVOKE_ERROR_TYPE("error","系统异常");

    private String code;
    private String desc;

    InvokeRecordStatusType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
