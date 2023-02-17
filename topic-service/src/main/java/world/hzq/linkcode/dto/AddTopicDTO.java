package world.hzq.linkcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.entity.TopicInput;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 添加题目的DTO对象
 * @date 2023/2/2 20:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("添加题目的传输对象")
public class AddTopicDTO {
    @ApiModelProperty("题解对象集合")
    private List<Solution> solution;
    @ApiModelProperty(value = "题目信息对象",required = true)
    private Topic topic;
    @ApiModelProperty(value = "输入集对象集合",required = true)
    private List<TopicInput> topicInput;
}
