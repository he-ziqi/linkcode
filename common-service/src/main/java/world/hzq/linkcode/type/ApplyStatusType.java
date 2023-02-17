package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 申请状态类型
 * @date 2023/2/12 20:56
 */
public enum ApplyStatusType {
    APPLY_STATUS_REJECT("reject","驳回申请"),
    APPLY_STATUS_PASS("pass","申请通过"),
    APPLY_STATUS_APPLYING("applying","审批中"),
    APPLY_STATUS_TIMEOUT("timeout","申请超时");

    private String code;
    private String desc;

    ApplyStatusType(String code, String desc) {
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
