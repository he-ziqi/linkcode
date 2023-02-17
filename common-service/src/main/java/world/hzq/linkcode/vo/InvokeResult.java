package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/3 21:31
 */
@ApiModel("执行结果实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvokeResult {
    @ApiModelProperty("通过用例数量")
    private Integer acCount;
    @ApiModelProperty("总用例数量")
    private Integer total;
    @ApiModelProperty("用例通过详情")
    private Boolean[] accept;
    @ApiModelProperty("是否通过")
    private Boolean isAccept;
    @ApiModelProperty("输出内容")
    private List<String> outputContent;
    @ApiModelProperty("输入内容")
    private List<String> inputContent;
    @ApiModelProperty("预期输出集合")
    private List<String> expectedOutput;
    @ApiModelProperty("执行用时")
    private Long timeSpent;
}
