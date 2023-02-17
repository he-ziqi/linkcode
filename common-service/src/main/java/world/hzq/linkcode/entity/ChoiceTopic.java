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
 * @date 2023/2/13 20:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("选择题信息实体")
public class ChoiceTopic {
    @ApiModelProperty("选择题记录id")
    private Long id;

    @ApiModelProperty("题目内容")
    private String content;

    @ApiModelProperty("题目答案")
    private String answer;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("截止时间")
    private Date stopTime;

    @ApiModelProperty("题目类型")
    private String type;

    @ApiModelProperty("作业状态")
    private String status;

}
