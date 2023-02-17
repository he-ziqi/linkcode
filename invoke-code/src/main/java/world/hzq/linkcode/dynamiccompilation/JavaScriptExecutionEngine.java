package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.util.Tools;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description javascript执行引擎-执行js代码
 * @date 2023/1/10 20:58
 */
public class JavaScriptExecutionEngine extends AbstractScriptLanguage{

    //不能直接调用方法获取 会创建不同的文件
    private String rootPath = getWriteRootFilePath();

    @Override
    public String getWriteFilePath() {
        return rootPath +
                CompilationConstraintsEnum.COMPILE_NAME_JAVASCRIPT.getValue() +
                CompilationConstraintsEnum.SUFFIX_FILE_SOURCE_JAVASCRIPT.getValue();
    }

    @Override
    protected String execute(String code, EncodeType encodeType, long timeout, TimeUnit timeUnit, Object... args) {
        //执行js文件
        ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName(LanguageType.JAVASCRIPT.getCode());
        if(jsEngine instanceof Invocable){
            Reader reader = null;
            try {
                reader = new FileReader(getWriteFilePath());
                jsEngine.eval(reader);
                Invocable invocable = (Invocable) jsEngine;
                //执行脚本函数获取结果
                Object res = invocable.invokeFunction(CompilationConstraintsEnum.COMPILE_NAME_METHOD.getValue(), args);
                if(res instanceof String){
                    return (String) res;
                }
            } catch (FileNotFoundException | ScriptException | NoSuchMethodException e) {
                return resolveExceptionMessage(e);
            } finally {
                Tools.closeStream(null,null,reader,null);
            }
            //返回值类型必须为String
            return CompilationConstraintsEnum.MESSAGE_EXCEPTION_RETURN_TYPE.getValue();
        }else{
            //脚本引擎不支持编译运行
            return CompilationConstraintsEnum.MESSAGE_EXCEPTION_ENGINE_SUPPORT.getValue();
        }
    }

    @Override
    protected String resultDispose(String result, EncodeType encodeType) {
        try {
            result = new String(result.getBytes(encodeType.getCode()));
        } catch (UnsupportedEncodingException e) {
            return CompilationConstraintsEnum.MESSAGE_ENCODE_NOT_SUPPORT.getValue();
        }
        return result;
    }

}
