package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.util.Tools;

/**
 * 动态编译过程中默认约定枚举
 */
public enum CompilationConstraintsEnum implements EnumType{
    //代码文件默认根路径
    CODE_ROOT_PATH("default_root_path","codes/"),
    SCANNER_ROOT_PATH("default_scanner_path",""),
    SCANNER_BUFFER_SIZE("default_buffer_size","512"),
    COMPILE_NAME_CLASS_JAVA("class_name_java","Solution"),
    COMPILE_NAME_JAVASCRIPT("file_name_javascript","Solution"),
    COMPILE_NAME_C("file_name_c","Solution"),
    INVOKE_FILE_NAME_C("invoke_file_name_c","Solution-c"),
    COMPILE_NAME_METHOD("method_name","main"),
    SUFFIX_FILE_SOURCE_JAVA("suffix_source_java",".java"),
    SUFFIX_FILE_SOURCE_C("suffix_source_c",".c"),
    SUFFIX_FILE_SOURCE_JAVASCRIPT("suffix_source_javascript",".js"),
    SUFFIX_FILE_CLASS("suffix_class",".class"),
    SEPARATOR_MESSAGE_EXCEPTION("exception_separator","\tat "),
    MESSAGE_EXCEPTION_CONDITION_NAME_CLASS("exception_condition_class_name","公共"),
    MESSAGE_EXCEPTION_CAUSED("exception_reason_title","Caused by : "),
    MESSAGE_EXCEPTION_MORE("exception_more"," more"),
    MESSAGE_EXCEPTION_RETURN_TYPE("exception_return_value_type","return value type must be String"),
    MESSAGE_EXCEPTION_ENGINE_SUPPORT("exception_engine_not_support","not supported by scripting engines"),
    MESSAGE_EXCEPTION_OMIT("exception_omit","\t... "),
    MESSAGE_MAKER_ROW("maker_row","\n"),
    TIME_SPENT("timeSpent","timeSpent="),
    MESSAGE_ENCODE_NOT_SUPPORT("exception_not_support_encode_way","编码方式不支持"),
    SEPARATOR_JAVA_CLASS_PATH("class_path_separator","."),
    SEPARATOR_FILE_CLASS_PATH("class_path_separator","/"),
    MESSAGE_INVALID_INPUT("invalid_input","无效输入"),
    MESSAGE_INTERRUPTED_EXCEPTION("interrupted_exception","程序中断"),
    MESSAGE_TIME_OUT("time_out","Time limit exceeded"),
    COMPILE_MAX_TIME("compile_max_time","5"),
    NOT_SUPPORT_CODE("not_support_code","当前不支持此编程语言"),
    ;
    private String code;
    private String value;

    CompilationConstraintsEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(EnumType type) {
        if(type instanceof CompilationConstraintsEnum){
            CompilationConstraintsEnum t = (CompilationConstraintsEnum) type;
            return this == t || (Tools.equals(this.getCode(),t.getCode()) && Tools.equals(this.getValue(),t.getValue()));
        }
        return false;
    }
}
