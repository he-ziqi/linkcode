package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzq
 * @version 1.0
 * @description 用户信息展示实体
 * @date 2023/2/8 19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户信息展示实体")
public class UserVO {
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户昵称")
    private String nickName;
    @ApiModelProperty("电话号码")
    private String phoneNumber;
    @ApiModelProperty("邮件地址")
    private String email;
    @ApiModelProperty("角色类型")
    private String roleType;
}
