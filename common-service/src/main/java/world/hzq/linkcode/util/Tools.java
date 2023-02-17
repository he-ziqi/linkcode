package world.hzq.linkcode.util;

import java.io.*;

/**
 * 通用工具类
 */
public class Tools {
    private Tools(){}

    public static boolean equals(String str1,String str2){
        if(str1 != null && str2 != null){
            return str1.trim().equals(str2.trim());
        }
        return str1 == null && str2 == null;
    }

    public static boolean isNull(String str){
        return str == null || "".equals(str) || "".equals(str.trim());
    }

    public static boolean isEmpty(String str){
        return "".equals(str);
    }

    public static boolean isNotNull(String str){
        return str != null && !"".equals(str);
    }

    public static boolean isNull(Object obj){
        return obj == null;
    }

    public static boolean isNotNull(Object obj){
        return obj != null;
    }

    public static int formatToInt(String str){
        return Integer.parseInt(str);
    }

    public static boolean contains(String source,String substring){
        return isNotNull(source) && isNotNull(substring) && source.contains(substring);
    }

    public static boolean responseOk(Integer code){
        return code == 200;
    }

    public static boolean isMail(String mail){
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return mail.matches(regex);
    }

    public static boolean isPhoneNumber(String phoneNumber){
        String regex = "^((13[0-9])|(14[05679])|(15([0-3,5-9]))|(16[2567])|(17[01235678])|(18[0-9]|19[135689]))\\d{8}$";
        return phoneNumber.matches(regex);
    }

    public static void destroyProcess(Process process){
        if(Tools.isNotNull(process)){
            process.destroy();
        }
    }

    //密码检验
    public static boolean pwdChecked(String password){
        return Tools.isNotNull(password) && password.length() >= 6 && password.length() <= 15;
    }

    public static boolean sameVerificationCode(String code1,String code2){
        return Tools.isNotNull(code1) && Tools.isNotNull(code2) &&
                Tools.equals(code1.trim().toUpperCase(),code2.trim().toUpperCase());
    }

    public static void closeStream(InputStream in, OutputStream out, Reader reader,Writer writer){
        if(Tools.isNotNull(in)){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Tools.isNotNull(out)){
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Tools.isNotNull(reader)){
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Tools.isNotNull(writer)){
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //电话信息脱敏
    public static String phoneNumberChange(String phoneNumber){
        int phoneNumberLength = phoneNumber.length();
        phoneNumber = phoneNumber.substring(0,phoneNumberLength / 3) + "***" + phoneNumber.substring(2 * phoneNumberLength / 3);
        return phoneNumber;
    }

    //邮件信息脱敏
    public static String emailChange(String email){
        int emailLength = email.length();
        email = email.substring(0,emailLength / 3) + "***" + email.substring(2 * emailLength / 3);
        return email;
    }
}
