package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 申请记录操作类型
 * @date 2023/2/13 00:19
 */
public enum ApplyRecordOperationType {
    APPLY_OPERATION_REJECT_TYPE("reject","驳回申请"),
    APPLY_OPERATION_PASS_TYPE("pass","申请通过");

    private String code;
    private String desc;

    ApplyRecordOperationType(String code, String desc) {
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
