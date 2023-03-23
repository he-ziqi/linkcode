package world.hzq.linkcode.dynamiccompilation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hzq
 * @version 1.0
 * @description 获取执行引擎的工厂
 * @date 2023/1/10 22:01
 */
public class ExecutionFactory {
    private static final Map<LanguageType,ExecutionEngine> engineMap = new HashMap<>();
    static {
        engineMap.put(LanguageType.JAVA,new JavaExecutionEngine());
        engineMap.put(LanguageType.JAVASCRIPT,new JavaScriptExecutionEngine());
        engineMap.put(LanguageType.C,new CExecutionEngine());
        engineMap.put(LanguageType.PYTHON,new PythonExecutionEngine());
    }

    public static ExecutionEngine getExecutionEngineByType(LanguageType languageType){
        ExecutionEngine engine = null;
        for (Map.Entry<LanguageType, ExecutionEngine> entry : engineMap.entrySet()) {
            if(entry.getKey().equals(languageType)){
                engine = entry.getValue();
            }
        }
        return engine;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getExecutionByClass(Class<T> cls){
        T engine = null;
        for (Map.Entry<LanguageType, ExecutionEngine> entry : engineMap.entrySet()) {
            if (entry.getValue().getClass().equals(cls)) {
                engine = (T) entry.getValue();
            }
        }
        return engine;
    }

}
