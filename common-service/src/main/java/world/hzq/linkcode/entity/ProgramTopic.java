package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/14 00:15
 */
@ApiModel("编程题信息实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramTopic {
    @ApiModelProperty("编程题记录id")
    private Long id;

    @ApiModelProperty("题目描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("截止时间")
    private Date stopTime;

    @ApiModelProperty("题目实体")
    private Topic topic;

    @ApiModelProperty("题目类型")
    private String type;

    @ApiModelProperty("作业状态")
    private String status;
}
