package world.hzq.linkcode.dynamiccompilation;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * @author hzq
 * @version 1.0
 * @description 抽象脚本语言
 * @date 2023/1/16 22:31
 */
public abstract class AbstractScriptLanguage implements ExecutionEngine{

    /**
     * 执行脚本代码
     * @param code 代码文本
     * @param encodeType 编码类型
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @param args 输入参数
     * @return 执行结果
     */
    protected final String invokeCode(String code, EncodeType encodeType, long timeout, TimeUnit timeUnit, Object... args){
        try {
            code = new String(code.getBytes(encodeType.getCode()));
        } catch (UnsupportedEncodingException e) {
            //不支持此编码格式
            return encodeType.getCode() + CompilationConstraintsEnum.MESSAGE_ENCODE_NOT_SUPPORT.getValue();
        }
        //将代码写入文件
        writeCodeToFile(code,getWriteFilePath());
        //执行代码文件
        String result = null;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        String finalCode = code;
        FutureTask<String> futureTask = new FutureTask<>(() -> execute(finalCode,encodeType,timeout,timeUnit,args));
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
            //执行异常,任务取消
            futureTask.cancel(true);
        } catch (TimeoutException e) {
            //执行超时
            result = CompilationConstraintsEnum.MESSAGE_TIME_OUT.getValue();
        } finally {
            //关闭线程池
            executor.shutdown();
        }
        //结果处理
        result = resultDispose(result,encodeType);
        return result;
    }

    protected abstract String execute(String code, EncodeType encodeType, long timeout, TimeUnit timeUnit, Object... args);

    protected abstract String resultDispose(String result, EncodeType encodeType);

    protected final String invokeCode(String code, long timeout, TimeUnit timeUnit, Object... args) {
        return invokeCode(code,EncodeType.UTF8,timeout,timeUnit,args);
    }

    @Override
    public String invoke(String code, EncodeType encodeType, long timeout, TimeUnit timeUnit, Object... args) {
        String result = null;
        try {
            result = invokeCode(new String(code.getBytes(encodeType.getCode())),encodeType, timeout,timeUnit,args);
        } catch (UnsupportedEncodingException e) {
            return encodeType.getCode() + CompilationConstraintsEnum.MESSAGE_ENCODE_NOT_SUPPORT.getValue();
        }
        return result;
    }

    @Override
    public String invoke(String code, long timeout, TimeUnit timeUnit, Object... args) {
        return invokeCode(code,timeout,timeUnit,args);
    }
}
