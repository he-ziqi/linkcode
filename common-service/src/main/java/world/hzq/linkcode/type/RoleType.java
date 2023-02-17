package world.hzq.linkcode.type;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 14:51
 */
public enum RoleType {
    ROLE_NAME_TEACHER("teacher_type","教师角色",Short.parseShort("1")),
    ROLE_NAME_STUDENT("student_type","学生角色",Short.parseShort("0"));

    private String code;
    private String desc;
    private Short authorityLevel;

    RoleType(String code, String desc,Short authorityLevel) {
        this.code = code;
        this.desc = desc;
        this.authorityLevel = authorityLevel;
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

    public Short getAuthorityLevel() {
        return authorityLevel;
    }

    public void setAuthorityLevel(Short authorityLevel) {
        this.authorityLevel = authorityLevel;
    }
}
