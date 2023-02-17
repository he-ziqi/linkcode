package world.hzq.linkcode.dynamiccompilation;

import org.springframework.context.ApplicationContext;
import world.hzq.linkcode.exception.CompileException;
import world.hzq.linkcode.feign.UserFeign;
import world.hzq.linkcode.util.ApplicationUtil;
import world.hzq.linkcode.util.FileUtil;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description 执行引擎-执行代码并返回结果
 * @date 2023/1/10 20:42
 */
public interface ExecutionEngine {

    /**
     * 写入代码到指定文件
     * @param code 代码字符串
     * @param filePath 文件路径
     */
    default void writeCodeToFile(String code,String filePath){
        //进行文件创建(存在则不创建)
        FileUtil.createFile(filePath);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(filePath)
                    )
            );
            if(code.startsWith("#include") && code.contains("int main")){
                int index = code.indexOf("int main");
                bw.write(code.substring(0,index));
                bw.newLine();
                bw.write(code.substring(index));
            }else {
                bw.write(code);
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Tools.closeStream(null,null,null,bw);
        }
    }


    /**
     * 获取代码文件写入的根目录路径
     * @return 写入根目录路径
     */
    default String getWriteRootFilePath(){
        ApplicationContext applicationContext = ApplicationUtil.getApplicationContext();
        UserFeign userFeign = applicationContext.getBean(UserFeign.class);
        Result<String> result = userFeign.getCompilePath();
        if(!Tools.responseOk(result.getCode())){
            System.out.println("获取用户编译路径异常");
            //使用默认路径
            return CompilationConstraintsEnum.CODE_ROOT_PATH.getCode();
        }
        return result.getData();
    }

    /**
     * 获取代码文件的写入路径
     * @return 写入路径
     */
    String getWriteFilePath();

    /**
     * 执行代码并获取执行结果
     * @param code 目标代码字符串
     * @param args 输入数据
     * @param encodeType 字符编码类型
     * @return 执行结果
     */
    String invoke(String code, EncodeType encodeType, long timeout, TimeUnit timeUnit,Object... args) throws CompileException;

    String invoke(String code,long timeout,TimeUnit timeUnit,Object... args) throws CompileException;

    /**
     * 解析异常信息并返回
     * @param e 异常对象
     * @return 异常信息
     */
    default String resolveExceptionMessage(Exception e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString())
                .append(CompilationConstraintsEnum.MESSAGE_MAKER_ROW.getValue());
        for (StackTraceElement element : stackTrace) {
            sb.append(CompilationConstraintsEnum.SEPARATOR_MESSAGE_EXCEPTION.getValue())
                    .append(element.toString())
                    .append(CompilationConstraintsEnum.MESSAGE_MAKER_ROW.getValue());
        }
        if(Tools.isNull(e.getCause())){
            return sb.toString();
        }
        String localizedMessage = e.getCause().toString();
        sb.append(CompilationConstraintsEnum.MESSAGE_EXCEPTION_CAUSED.getValue())
                .append(localizedMessage)
                .append(CompilationConstraintsEnum.MESSAGE_MAKER_ROW.getValue());
        StackTraceElement[] causedStackTrace = e.getCause().getStackTrace();
        sb.append(CompilationConstraintsEnum.SEPARATOR_MESSAGE_EXCEPTION.getValue())
                .append(causedStackTrace[0].toString())
                .append(CompilationConstraintsEnum.MESSAGE_MAKER_ROW.getValue());
        if(causedStackTrace.length > 1){
            sb.append(CompilationConstraintsEnum.MESSAGE_EXCEPTION_OMIT.getValue())
                    .append(causedStackTrace.length - 1)
                    .append(CompilationConstraintsEnum.MESSAGE_EXCEPTION_MORE.getValue());
        }
        return sb.toString();
    }
}
