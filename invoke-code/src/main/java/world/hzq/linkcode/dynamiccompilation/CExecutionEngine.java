package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.util.Tools;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description C语言执行引擎
 * @date 2023/1/10 20:38
 */
public class CExecutionEngine extends AbstractProcessActuators{

    //不能直接调用方法获取 会创建不同的文件
    private String rootPath = getWriteRootFilePath();

    @Override
    public String getWriteFilePath() {
        return rootPath +
                CompilationConstraintsEnum.COMPILE_NAME_C.getValue() +
                CompilationConstraintsEnum.SUFFIX_FILE_SOURCE_C.getValue();
    }

    /**
     * @description 对最终返回结果的处理 默认不处理
     * @param result 执行结果
     * @param encodeType
     * @return java.lang.String 处理后的结果
     * @author hzq
     * @date 2023/1/16 21:31
     */
    @Override
    protected String resultDispose(String result, EncodeType encodeType) {
        return result;
    }

    /**
     * @description 进行C的代码执行
     * @param code 代码
     * @param inputData 输入数据
     * @param encodeType 编码类型
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return java.lang.String 执行结果
     * @author hzq
     * @date 2023/1/16 21:28
     */
    @Override
    protected String execute(String code, String inputData, EncodeType encodeType, long timeout, TimeUnit timeUnit) throws UnsupportedEncodingException {
        String executeCommand = "./" +
                rootPath +
                CompilationConstraintsEnum.INVOKE_FILE_NAME_C.getValue();
        return executeProcessCommand(executeCommand, encodeType, new String(inputData.getBytes(encodeType.getCode())),timeout,timeUnit);
    }

    /**
     * @description 进行C代码的编译
     * @param inputData 输入数据
     * @param encodeType 编码类型
     * @return byte[] 编译结果 null表示编译正常
     * @author hzq
     * @date 2023/1/16 21:29
     */
    @Override
    protected byte[] compile(String inputData, EncodeType encodeType) throws UnsupportedEncodingException {
        //进行编译
        String compileCommand = "gcc -o " +
                rootPath +
                CompilationConstraintsEnum.INVOKE_FILE_NAME_C.getValue() + " " +
                rootPath +
                CompilationConstraintsEnum.COMPILE_NAME_C.getValue() +
                CompilationConstraintsEnum.SUFFIX_FILE_SOURCE_C.getValue();
        String compileResult = compileCode(compileCommand, encodeType);
        //gcc -o codes/Solution-c codes/Solution.c
        if(!Tools.isNull(compileResult)){
            //编译出错,返回结果
            return compileResult.getBytes(encodeType.getCode());
        }
        return null;
    }

}
