package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 作业实体
 * @date 2023/2/14 00:13
 */
@ApiModel("作业信息实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    @ApiModelProperty("班级实体")
    private ClassRoom classRoom;

    @ApiModelProperty("教师实体")
    private User teacher;

    @ApiModelProperty("选择题题目信息列表")
    private List<ChoiceTopic> choiceTopicList;

    @ApiModelProperty("编程题题目信息列表")
    private List<ProgramTopic> programTopicList;

    @ApiModelProperty("选择题总数")
    private Long choiceTopicTotal;

    @ApiModelProperty("编程题总数")
    private Long programTopicTotal;

}
