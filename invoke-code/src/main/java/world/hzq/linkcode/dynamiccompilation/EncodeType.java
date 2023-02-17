package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.util.Tools;

public enum EncodeType implements EnumType{
    GB2312("GB2312","中文字符集"),
    GBK("GBK","中文字符集"),
    BIG5("Big5","繁体中文字符集"),
    UNICODE("Unicode","unicode"),
    UTF8("UTF-8","utf-8"),
    BASE64("Base64","Base64")
    ;
    private String code;
    private String name;

    EncodeType(String code, String name) {
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
        if(type instanceof EncodeType){
            EncodeType t = (EncodeType) type;
            return this == t || (Tools.equals(this.getCode(),t.getCode()) && Tools.equals(this.getName(),t.getName()));
        }
        return false;
    }
}
