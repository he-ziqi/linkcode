package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hzq
 * @version 1.0
 * @description 题解实体
 * @date 2023/2/2 20:25
 */
@ApiModel("题解实体对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solution implements Serializable {
    @ApiModelProperty("题解主键")
    private Long id;
    @ApiModelProperty("题解代码")
    private String answerCode;
    @ApiModelProperty("题解代码类型")
    private String languageType;
    @ApiModelProperty("题解描述")
    private String comment;
    @ApiModelProperty("题目id")
    private Long topicId;
    @ApiModelProperty("用户id")
    private Long userId = -1L; //默认等于-1
    @ApiModelProperty("创建时间")
    private Date createTime;
}
