package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 题目类型
 * @date 2023/2/14 14:25
 */
public enum TopicType {
    TOPIC_TYPE_CHOICE("choice_topic_type","选择题类型"),
    TOPIC_TYPE_PROGRAM("program_topic_type","编程题类型");

    private String code;
    private String desc;

    TopicType(String code, String desc) {
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
