package world.hzq.linkcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzq
 * @version 1.0
 * @description 提交作业
 * @date 2023/2/14 15:58
 */
@ApiModel("提交作业信息传输实体")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubmitJobDTO {
    @ApiModelProperty(value = "作业记录id",required = false)
    private Long id;

    @ApiModelProperty(value = "题目id",required = true)
    private Long jobId;

    @ApiModelProperty(value = "作业内容",required = true)
    private String content;

    @ApiModelProperty(value = "作业类型",required = true)
    private String type;

    @ApiModelProperty(value = "编程语言类型",required = false)
    private String languageType;

    @ApiModelProperty(value = "班级id",required = true)
    private Long classRoomId;
}
