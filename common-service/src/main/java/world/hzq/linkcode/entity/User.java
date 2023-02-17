package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/6 16:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户信息实体")
public class User implements Serializable {
    @ApiModelProperty(value = "用户id",required = false)
    private Long id;
    @ApiModelProperty("用户昵称")
    private String nickName;
    @ApiModelProperty(value = "用户头像地址",required = false)
    private String avatarAddr;
    @ApiModelProperty("电话号码")
    private String phoneNumber;
    @ApiModelProperty("邮件地址")
    private String email;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("角色类型")
    private String roleType;
}
