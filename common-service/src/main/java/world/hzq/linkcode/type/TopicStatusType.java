package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 题目状态类型
 * @date 2023/2/10 23:14
 */
public enum TopicStatusType {
    TOPIC_STATUS_UNTRIED("untried","未执行过"),
    TOPIC_STATUS_PASS("pass","通过"),
    TOPIC_STATUS_NOT_PASS("not_pass","执行过,但未通过");

    private String code;
    private String desc;

    TopicStatusType(String code, String desc) {
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
