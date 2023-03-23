package world.hzq.linkcode.dynamiccompilation;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description Python执行引擎
 * @date 2023/3/23 16:24
 */
public class PythonExecutionEngine extends AbstractProcessActuators{

    //不能直接调用方法获取 会创建不同的文件
    private String rootPath = getWriteRootFilePath();

    @Override
    public String getWriteFilePath() {
        return rootPath +
                CompilationConstraintsEnum.COMPILE_NAME_PYTHON.getValue() +
                CompilationConstraintsEnum.SUFFIX_FILE_SOURCE_PYTHON.getValue();
    }

    @Override
    protected String resultDispose(String result, EncodeType encodeType) {
        return result;
    }

    @Override
    protected String execute(String code, String inputData, EncodeType encodeType, long timeout, TimeUnit timeUnit) throws UnsupportedEncodingException {
        String command = "python3 " +
                rootPath +
                CompilationConstraintsEnum.INVOKE_FILE_NAME_PYTHON.getValue() +
                CompilationConstraintsEnum.SUFFIX_FILE_SOURCE_PYTHON.getValue();
        return executeProcessCommand(command,encodeType,inputData,timeout,timeUnit);
    }

    //python不需要编译 直接返回null表示编译通过
    @Override
    protected byte[] compile(String inputData, EncodeType encodeType) throws UnsupportedEncodingException {
        return null;
    }

}
