package world.hzq.linkcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzq
 * @version 1.0
 * @description 用户登录传输对象
 * @date 2023/2/6 19:54
 */
@ApiModel("用户登录信息实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @ApiModelProperty("昵称(用户名)")
    private String nickName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号码")
    private String phoneNumber;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("登录方式")
    private String way;

    @ApiModelProperty("验证码")
    private String verificationCode;

    @ApiModelProperty("角色类型")
    private String roleType;
}
