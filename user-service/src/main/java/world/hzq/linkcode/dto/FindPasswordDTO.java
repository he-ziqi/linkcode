package world.hzq.linkcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzq
 * @version 1.0
 * @description 找回密码信息传输对象
 * @date 2023/2/9 10:29
 */
@Data
@ApiModel("找回密码信息传输对象")
@NoArgsConstructor
@AllArgsConstructor
public class FindPasswordDTO {
    @ApiModelProperty("手机号码")
    private String phoneNumber;

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("验证码")
    private String verificationCode;

    @ApiModelProperty("邮件地址")
    private String mail;
}
