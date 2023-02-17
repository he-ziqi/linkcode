package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.util.Tools;

/**
 * @author hzq
 * @version 1.0
 * @description 动态编译支持语言类型
 * @date 2023/1/10 17:32
 */
public enum LanguageType implements EnumType{
    JAVA("Java","dynamic_compile_java"),
    JAVASCRIPT("JavaScript","script_engine_javascript"),
    C("C","process_compile_c"),
    ;
    private String code;
    private String name;

    LanguageType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(EnumType type) {
        if(type instanceof LanguageType){
            LanguageType t = (LanguageType) type;
            return this == t || (Tools.equals(this.getCode(),t.getCode()) && Tools.equals(this.getName(),t.getName()));
        }
        return false;
    }
}
