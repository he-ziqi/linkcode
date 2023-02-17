package world.hzq.linkcode.es.index;

/**
 * @author hzq
 * @version 1.0
 * @description 索引类型枚举
 * @date 2023/2/5 03:05
 */
public enum ElasticSearchIndexType {
    INDEX_TOPIC_INFO("topic_info","题目信息索引"),
    INDEX_TOPIC_SOLUTION("topic_solution","题解索引"),
    INDEX_SOLUTION_RECORD("solution_record","题解记录索引");
    private String code;
    private String name;

    ElasticSearchIndexType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
