package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hzq
 * @version 1.0
 * @description 题目实体
 * @date 2023/2/2 20:23
 */
@ToString
@ApiModel("题目信息实体对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic implements Serializable {
    @ApiModelProperty("题目主键")
    private Long id;
    @ApiModelProperty("题目名称")
    private String topicName;
    @ApiModelProperty("题目描述")
    private String topicComment;
    @ApiModelProperty("难度级别")
    private Byte difficultyLevel;
    @ApiModelProperty("时间限制ms")
    private Integer timeout;
    @ApiModelProperty("题目状态")
    private String status;
}
