package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzq
 * @version 1.0
 * @description 作业提交结果vo
 * @date 2023/2/16 17:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("题目提交结果展示实体")
public class TopicSubmitResultVO {

    @ApiModelProperty("提交执行结果是否正确")
    private Boolean isAccepted;

    @ApiModelProperty("编程题执行结果")
    private InvokeResult invokeResult;

    @ApiModelProperty("题目类型")
    private String topicType;
}
