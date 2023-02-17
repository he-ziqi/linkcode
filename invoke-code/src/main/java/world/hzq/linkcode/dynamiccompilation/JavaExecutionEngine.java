package world.hzq.linkcode.dynamiccompilation;

import world.hzq.linkcode.util.Tools;

import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description Java代码执行引擎
 * @date 2023/1/10 20:49
 */
public class JavaExecutionEngine extends AbstractCompiledLanguage{
    //保存标准输入流和输出流 用于后续归还标准流
    private static InputStream STANDARD_IN = System.in;
    private static PrintStream STANDARD_OUT = System.out;

    //不能直接调用方法获取 会创建不同的文件
    private String rootPath = getWriteRootFilePath();

    @Override
    public String getWriteFilePath() {
        return rootPath +
                CompilationConstraintsEnum.COMPILE_NAME_CLASS_JAVA.getValue() +
                CompilationConstraintsEnum.SUFFIX_FILE_SOURCE_JAVA.getValue();
    }

    /**
     * Java代码编译
     * @param inputData 输入数据
     * @param encodeType 编码类型
     * @return 编译结果 null表示正常编译
     * @throws UnsupportedEncodingException 不支持的编码类型
     */
    @Override
    protected byte[] compile(String inputData, EncodeType encodeType) throws UnsupportedEncodingException {
        InputStream in = null;
        in = new ByteArrayInputStream(inputData.getBytes(encodeType.getCode()));
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        //改变标准输入流方向
        System.setIn(in);
        //编译.java文件
        byte[] compileResult = compileJava(getWriteFilePath(),
                in,
                err);
        if(Tools.isNotNull(compileResult)){ //编译错误,返回错误信息
            String res = new String(compileResult);
            if(Tools.contains(res,CompilationConstraintsEnum.MESSAGE_EXCEPTION_CONDITION_NAME_CLASS.getValue())){
                res = "类名必须是" + CompilationConstraintsEnum.COMPILE_NAME_CLASS_JAVA.getValue();
            }
            return res.getBytes(encodeType.getCode());
        }
        Tools.closeStream(in,null,null,null);
        return null;
    }

    /**
     * 执行Java代码
     * @param code Java代码
     * @param inputData 输入数据
     * @param encodeType 编码类型
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 执行结果
     */
    @Override
    protected String execute(String code, String inputData, EncodeType encodeType, long timeout, TimeUnit timeUnit) {
        ByteArrayOutputStream resultOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(resultOut);
        //改变标准输出流方向
        System.setOut(out);
        //执行class文件
        String exceptionMessage = executeJavaFile(rootPath,
                CompilationConstraintsEnum.COMPILE_NAME_CLASS_JAVA.getValue(),
                CompilationConstraintsEnum.COMPILE_NAME_METHOD.getValue());
        if(Tools.isNotNull(exceptionMessage)){ //程序执行出现异常
            return exceptionMessage;
        }
        Tools.closeStream(null,out,null,null);
        return resultOut.toString();
    }

    /**
     * 动态编译Java代码
     * @return true表示编译成功
     */
    private static byte[] compileJava(String filePath,InputStream in,ByteArrayOutputStream err) {
        return ToolProvider.
                getSystemJavaCompiler().
                run(in, null, err,filePath) == 0
                ? null
                : err.toByteArray();
    }

    /**
     * 执行Java文件
     * @param directory 文件目录
     * @param className 类名
     * @param methodName 目标方法名
     * @return 执行过程中的异常信息
     */
    private String executeJavaFile(String directory,String className,String methodName){
        DynamicCompileClassLoader classLoader = new DynamicCompileClassLoader(directory);
        String result = null;
        try {
            Class<?> classInstance = classLoader.loadClass(className);
            Object obj = classInstance.newInstance();
            Method mainMethod = classInstance.getDeclaredMethod(methodName, String[].class);
            mainMethod.invoke(obj, (Object) new String[0]);
        } catch (ClassNotFoundException
                | IllegalAccessException
                | InstantiationException
                | NoSuchMethodException
                | InvocationTargetException e) {
            if(e instanceof ClassNotFoundException){
                result = "类名必须是" + className;
            }
            if(e instanceof NoSuchMethodException){
                result = "方法名必须是" + methodName + ",且参数类型必须是String[]";
            }
            if(e instanceof InvocationTargetException){
                result = resolveExceptionMessage(e);
            }
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 结果处理
     * @param result 执行结果(默认不处理)
     * @param encodeType 编码类型
     * @return 处理后的结果
     */
    @Override
    protected String resultDispose(String result, EncodeType encodeType) {
        //改回标准输入流和输出流方向
        System.setOut(STANDARD_OUT);
        System.setIn(STANDARD_IN);
        return result;
    }

    /**
     * 自定义类加载器,从指定目录下加载类文件
     */
    private static class DynamicCompileClassLoader extends ClassLoader{
        private String path;

        public DynamicCompileClassLoader(String directory) {
            super();
            this.path = directory;
        }

        public DynamicCompileClassLoader(ClassLoader parent,String directory){
            super(parent);
            this.path = directory;
        }

        //不指定类加载目录则从项目根目录下加载
        public DynamicCompileClassLoader(){
            this.path = CompilationConstraintsEnum.SCANNER_ROOT_PATH.getValue();
        }

        /**
         * 重写类加载的路径
         * @param name 类加载的根路径
         */
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            String classPath = this.path +
                    name.replace(CompilationConstraintsEnum.SEPARATOR_JAVA_CLASS_PATH.getValue(),
                            CompilationConstraintsEnum.SEPARATOR_FILE_CLASS_PATH.getValue()) +
                    CompilationConstraintsEnum.SUFFIX_FILE_CLASS.getValue();
            byte[] data = null;
            byte[] buffer = new byte[Tools.formatToInt(CompilationConstraintsEnum.SCANNER_BUFFER_SIZE.getValue())];
            ByteArrayOutputStream bos = null;
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(classPath));
                bos = new ByteArrayOutputStream();
                int readCnt = 0;
                while((readCnt = is.read(buffer)) != -1){
                    bos.write(buffer,0,readCnt);
                }
                data = bos.toByteArray();
                return defineClass(name,data,0,data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                Tools.closeStream(is,bos,null,null);
            }
            return super.findClass(name);
        }
    }
}
