package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.util.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description 抽象进程执行器
 * @date 2023/1/16 17:48
 */
public abstract class AbstractProcessActuators extends AbstractCompiledLanguage{

    /**
     * @description 进行代码文件的编译
     * @param command 编译命令
     * @param encodeType 编码类型
     * @return java.lang.String 执行结果
     * @author hzq
     * @date 2023/1/16 18:39
     */
    protected String compileCode(String command, EncodeType encodeType){
        Process process = null;
        BufferedReader error = null;
        StringBuilder result = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec(command,null, null);
            //编译代码最长时间5s
            process.waitFor(Tools.formatToInt(CompilationConstraintsEnum.COMPILE_MAX_TIME.getValue()),TimeUnit.SECONDS);
            error = new BufferedReader(
                    new InputStreamReader(
                            process.getErrorStream(),
                            encodeType.getCode()
                    )
            );
            String read = null;
            while((read = error.readLine()) != null){
                result.append(read).append(CompilationConstraintsEnum.MESSAGE_MAKER_ROW.getValue());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            Tools.closeStream(null,null,error,null);
            Tools.destroyProcess(process);
        }
        return result.toString();
    }

    /**
     * @description 执行进程命令并获取执行结果
     * @param command 命令
     * @param inputData 输入数据
     * @return java.lang.String 执行结果
     * @author hzq
     * @date 2023/1/16 17:51
     */
    protected final String executeProcessCommand(String command, EncodeType encodeType, String inputData,long timeout,TimeUnit timeUnit){
        //执行命令,返回子进程对象
        Process process = null;
        OutputStream out = null;
        BufferedReader in = null;
        BufferedReader error = null;
        StringBuilder result = new StringBuilder();
        try {
            //执行命令
            process = Runtime.getRuntime().exec(command, null, null);
            //数据输入
            out = process.getOutputStream();
            out.write(inputData.getBytes(encodeType.getCode()));
            out.flush();
            //这里必须关闭子进程的输入流,否则会卡住
            out.close();
            //等待命令执行完成
            boolean success = process.waitFor(timeout, timeUnit);
            if(!success){
                return CompilationConstraintsEnum.MESSAGE_TIME_OUT.getValue();
            }
            in = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream(),
                            encodeType.getCode()
                    )
            );
            error = new BufferedReader(
                    new InputStreamReader(
                            process.getErrorStream(),
                            encodeType.getCode()
                    )
            );
            String read = null;
            while((read = in.readLine()) != null){
                result.append(read).append(CompilationConstraintsEnum.MESSAGE_MAKER_ROW.getValue());
            }
            while((read = error.readLine()) != null){
                result.append(read).append(CompilationConstraintsEnum.MESSAGE_MAKER_ROW.getValue());
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流和销毁子进程
            Tools.closeStream(null,out,in,null);
            Tools.closeStream(null,null,error,null);
            Tools.destroyProcess(process);
        }
        return result.toString();
    }
}
