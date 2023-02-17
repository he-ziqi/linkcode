package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.entity.User;

import java.util.Date;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 21:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("申请记录信息展示实体")
public class ApplyRecordVO {
    @ApiModelProperty("记录id")
    private Long id;
    @ApiModelProperty("申请状态")
    private String applyStatus;
    @ApiModelProperty("申请时间")
    private Date applyTime;
    @ApiModelProperty("申请过期时间")
    private Date expirationTime;
    @ApiModelProperty("申请班级")
    private ClassRoom classRoom;
    @ApiModelProperty("申请学生")
    private User applyStudent;
    @ApiModelProperty("审批教师")
    private User teacher;
    @ApiModelProperty("班级内学生昵称")
    private String nickName;
}
