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
 * @date 2023/2/9 22:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("执行记录传输实体")
public class SolutionRecord {
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("题目id")
    private Long topicId;

    @ApiModelProperty("题目状态类型(exception,error,pass,not_pass）")
    private String topicStatusType;

    @ApiModelProperty("执行信息")
    private String msg;

    @ApiModelProperty("代码内容")
    private String codeContent;

    @ApiModelProperty("语言类型")
    private String languageType;

    @ApiModelProperty("执行时间")
    private Date updateTime;

    @ApiModelProperty("通过用例数量")
    private Integer acceptCount;

    @ApiModelProperty("执行耗时")
    private Long invokeTime;
}
