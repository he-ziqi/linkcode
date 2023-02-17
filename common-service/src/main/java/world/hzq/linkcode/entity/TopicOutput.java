package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/3 14:47
 */
@ApiModel("输出集实体")
public class TopicOutput implements Serializable {
    @ApiModelProperty("输出集主键")
    private Long id;
    @ApiModelProperty("输出集内容")
    private String outputContent;
    @ApiModelProperty("关键的输入集主键")
    private Long topicInputId;

    public TopicOutput(Long id, String outputContent, Long topicInputId) {
        this.id = id;
        this.outputContent = outputContent;
        this.topicInputId = topicInputId;
    }

    public TopicOutput() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutputContent() {
        return outputContent;
    }

    public void setOutputContent(String outputContent) {
        this.outputContent = outputContent;
    }

    public Long getTopicInputId() {
        return topicInputId;
    }

    public void setTopicInputId(Long topicInputId) {
        this.topicInputId = topicInputId;
    }
}
