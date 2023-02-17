package world.hzq.linkcode.es;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.util.Tools;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzq
 * @version 1.0
 * @description 抽象基础es操作执行器
 * @date 2023/2/5 18:00
 */
@Component
public abstract class BaseElasticOperationExecutor {
    @Resource
    public RestHighLevelClient restHighLevelClient;

    /**
     * @description 将实体对象转换为Script
     * @param: obj 实体对象
     * @return: org.elasticsearch.script.Script
     * @author hzq
     * @date 2023/2/5 19:29
     */
    public Script convertToScript(Object obj){
        if(Tools.isNull(obj)){
            throw new RuntimeException("实体对象为null,转换为Script失败");
        }
        Script script = null;
        //保存对象的属性名和属性值
        Map<String,Object> params = new HashMap<>();
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        StringBuilder sourceBuilder = new StringBuilder();
        for (Field field : fields) {
            //暴力反射
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                //获取属性值
                Object fieldValue = field.get(obj);
                //实现部分更新,不为null才添加
                //String source = "ctx._source.fieldName = params.fieldValue;ctx._source.fieldName = params.fieldValue";
                if(Tools.isNotNull(fieldValue)){
                    params.put(fieldName,fieldValue);
                    sourceBuilder.append("ctx._source.")
                            .append(fieldName)
                            .append(" = ")
                            .append("params")
                            .append(".")
                            .append(fieldName)
                            .append(";");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //设置Script
        script = new Script(ScriptType.INLINE,"painless",sourceBuilder.toString(),params);
        return script;
    }
}
