package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author hzq
 * @version 1.0
 * @description 题目输入集实体
 * @date 2023/2/2 20:27
 */
@ApiModel("输入集实体")
public class TopicInput implements Serializable {
    @ApiModelProperty("输入集主键")
    private Long id;
    @ApiModelProperty("输入内容")
    private String inputContent;
    @ApiModelProperty("题目id")
    private Long topicId;

    public TopicInput(Long id, String inputContent, Long topicId) {
        this.id = id;
        this.inputContent = inputContent;
        this.topicId = topicId;
    }

    public TopicInput() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInputContent() {
        return inputContent;
    }

    public void setInputContent(String inputContent) {
        this.inputContent = inputContent;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
