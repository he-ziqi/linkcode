package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 作业提交状态类型
 * @date 2023/2/14 14:29
 */
public enum JobCommitStatusType {
    JOB_STATUS_UNCOMMIT("uncommit_type","未提交"),
    JOB_STATUS_COMMIT("commit_type","已提交"),
    JOB_STATUS_TIMEOUT("timeout_type","超时未提交");

    private String code;
    private String desc;

    JobCommitStatusType(String code, String desc) {
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
