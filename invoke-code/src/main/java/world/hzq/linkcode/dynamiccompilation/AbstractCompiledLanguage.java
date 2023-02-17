package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.exception.CompileException;
import world.hzq.linkcode.util.Tools;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * @author hzq
 * @version 1.0
 * @description 抽象编译型语言
 * @date 2023/1/16 20:29
 */
public abstract class AbstractCompiledLanguage implements ExecutionEngine{

    /**
     * 执行代码
     * @param code 代码
     * @param encodeType 编码类型
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @param inputData 输入数据
     * @return 执行结果
     */
    public final String invokeCode(String code, EncodeType encodeType, long timeout, TimeUnit timeUnit,String inputData) throws CompileException{
        try {
            //对源代码内容进行编码
            code = new String(code.getBytes(encodeType.getCode()));
        } catch (UnsupportedEncodingException e) {
            //不支持此编码格式
            return encodeType.getCode() + CompilationConstraintsEnum.MESSAGE_ENCODE_NOT_SUPPORT.getValue();
        }
        //将代码写入文件
        writeCodeToFile(code,getWriteFilePath());
        //编译代码文件
        byte[] compileResult = null;
        try {
            compileResult = compile(inputData,encodeType);
        } catch (UnsupportedEncodingException e) {
            //不支持此编码格式
            return encodeType.getCode() + CompilationConstraintsEnum.MESSAGE_ENCODE_NOT_SUPPORT.getValue();
        }
        if(Tools.isNotNull(compileResult)){ //编译错误,返回错误信息
            try {
                throw new CompileException("编译错误,msg:" + new String(compileResult,encodeType.getCode()));
            } catch (UnsupportedEncodingException e) {
                //不支持此编码格式
                return encodeType.getCode() + CompilationConstraintsEnum.MESSAGE_ENCODE_NOT_SUPPORT.getValue();
            }
        }
        //执行代码文件
        String result = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String finalCode = code;
        FutureTask<String> futureTask = new FutureTask<>(() -> execute(finalCode,inputData,encodeType,timeout,timeUnit));
        executor.execute(futureTask);
        try {
            long start = System.currentTimeMillis();
            result = futureTask.get(timeout,timeUnit);
            long end = System.currentTimeMillis();
            //程序执行到这里 说明无异常,给结尾换行增加执行时长
            result = result + CompilationConstraintsEnum.TIME_SPENT.getValue() + (end - start);
        } catch (InterruptedException e) {
            result = CompilationConstraintsEnum.MESSAGE_INTERRUPTED_EXCEPTION.getValue();
        } catch (ExecutionException e) {
            //执行异常,取消任务
            futureTask.cancel(true);
        } catch (TimeoutException e) {
            result = CompilationConstraintsEnum.MESSAGE_TIME_OUT.getValue();
            futureTask.cancel(true);
        } finally {
            //关闭线程池
            executor.shutdown();
        }
        //结果处理
        result = resultDispose(result,encodeType);
        return result;
    }

    @Override
    public String invoke(String code, EncodeType encodeType, long timeout, TimeUnit timeUnit, Object... args)throws CompileException {
        //文件执行的输入是一个字符串
        if (args.length == 1 && args[0] instanceof String) {
            String inputData = (String) args[0];
            return invokeCode(code,encodeType,timeout,timeUnit,inputData);
        }
        return CompilationConstraintsEnum.MESSAGE_INVALID_INPUT.getValue();
    }

    @Override
    public String invoke(String code, long timeout,TimeUnit timeUnit,Object... args) throws CompileException{
        return invoke(code,EncodeType.UTF8,timeout,timeUnit,args);
    }

    protected abstract String resultDispose(String result, EncodeType encodeType);

    protected abstract String execute(String code, String inputData, EncodeType encodeType, long timeout, TimeUnit timeUnit) throws UnsupportedEncodingException;

    protected abstract byte[] compile(String inputData, EncodeType encodeType) throws UnsupportedEncodingException;

    public final String invokeCode(String code, long timeout, TimeUnit timeUnit,String inputData)throws CompileException{
        return invoke(code,EncodeType.UTF8,timeout,timeUnit,inputData);
    }
}
