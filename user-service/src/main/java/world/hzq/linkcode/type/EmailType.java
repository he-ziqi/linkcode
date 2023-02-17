package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description 邮件类型
 * @date 2023/2/9 01:02
 */
public enum EmailType {
    LOGIN_OPERATION_TYPE("login_operation_type","登录类型"),
    FIND_PASSWORD_OPERATION_TYPE("find_password_operation_type","找回密码类型"),
    UPDATE_PASSWORD_OPERATION_TYPE("update_password_operation_type","更新密码类型");

    private String code;
    private String desc;

    EmailType(String code, String desc) {
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
