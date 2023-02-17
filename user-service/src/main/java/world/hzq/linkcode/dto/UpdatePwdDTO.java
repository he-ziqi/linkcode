package world.hzq.linkcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzq
 * @version 1.0
 * @description 修改密码dto
 * @date 2023/2/9 00:54
 */
@ApiModel("修改密码信息传输实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePwdDTO {
    @ApiModelProperty("旧密码")
    private String oldPwd;
    @ApiModelProperty("新密码")
    private String newPwd;
    @ApiModelProperty("邮箱验证码")
    private String verificationCode;
}
