package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hzq
 * @version 1.0
 * @description 班级实体
 * @date 2023/2/12 17:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("班级信息实体")
public class ClassRoom {
    @ApiModelProperty("班级id")
    private Long id;
    @ApiModelProperty("班级名称")
    private String name;
    @ApiModelProperty("班级公告")
    private String accouncement;
    @ApiModelProperty("班级人数")
    private Integer number;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("创建人")
    private User teacher;
}
