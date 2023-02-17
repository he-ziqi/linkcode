package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 登录类型
 * @date 2023/2/6 20:01
 */
public enum LoginType {
    NICK_NAME_TYPE("nick_name_type","昵称密码登录方式"),
    PHONE_PASSWORD_TYPE("phone_password_type","手机号密码登录方式"),
    EMAIL_VERIFICATION_TYPE("email_verification_type","邮箱验证码方式登录");

    private String code;
    private String desc;

    LoginType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
