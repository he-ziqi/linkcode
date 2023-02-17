package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import world.hzq.linkcode.entity.User;

import java.util.Date;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 23:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("学生信息展示实体")
public class ClassStudentVO {
    @ApiModelProperty("学生信息")
    private User student;
    @ApiModelProperty("加入时间")
    private Date joinTime;
    @ApiModelProperty("在课堂中的昵称")
    private String nickName;
}
