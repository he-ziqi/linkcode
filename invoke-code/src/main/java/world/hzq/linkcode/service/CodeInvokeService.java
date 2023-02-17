package world.hzq.linkcode.service;

import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InvokeResult;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 17:33
 */
public interface CodeInvokeService {
    /**
     * @description 执行指定语言的代码
     * @param: code 代码字符串
     * @param: languageType 语言类型
     * @param: topicId 题目id
     * @return InvokeResult 执行结果实体
     * @author hzq
     * @date 2023/2/3 16:53
     */
    Result<InvokeResult> invokeCode(String code, String languageType, Long topicId);

    /**
     * @description 获取支持的编程语言集合
     * @return: world.hzq.sjm.util.Result<java.util.Map<java.lang.String,java.lang.String>>
     * @author hzq
     * @date 2023/2/4 03:26
     */
    Result<List<String>> getSupportLanguage();

    /**
     * @description 保存执行记录
     * @param code 代码
     * @param languageType 语言类型
     * @param topicId 题目id
     * @param invokeResult 代码执行结果
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/9 23:08
     */
    Result<String> saveRecord(String code, String languageType, Long topicId, Result<InvokeResult> invokeResult);
}
