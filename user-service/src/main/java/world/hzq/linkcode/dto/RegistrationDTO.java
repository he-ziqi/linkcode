package world.hzq.linkcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzq
 * @version 1.0
 * @description 注册信息DTO
 * @date 2023/2/12 14:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("注册信息传输实体")
public class RegistrationDTO {
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("邮件地址")
    private String email;
    @ApiModelProperty("电话号码")
    private String phoneNumber;
    @ApiModelProperty("角色类型")
    private String roleType;
}
