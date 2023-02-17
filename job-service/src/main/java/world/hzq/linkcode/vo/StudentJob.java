package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import world.hzq.linkcode.entity.Job;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/14 03:09
 */
@ApiModel("学生作业信息展示实体")
public class StudentJob extends Job {

    @ApiModelProperty("选择题作业状态列表")
    private List<String> choiceTopicStatusList;

    @ApiModelProperty("编程题作业状态列表")
    private List<String> programTopicStatusList;
}
