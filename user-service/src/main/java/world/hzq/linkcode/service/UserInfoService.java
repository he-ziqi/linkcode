package world.hzq.linkcode.service;

import world.hzq.linkcode.entity.SolutionRecord;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InfoVO;
import world.hzq.linkcode.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/8 21:06
 */
public interface UserInfoService {
    /**
     * @description 获取用户信息 必须要登录后携带token才可获取
     * @return: world.hzq.sjm.util.Result<world.hzq.sjm.vo.UserVO>
     * @author hzq
     * @date 2023/2/8 21:01
     */
    Result<UserVO> getUserInfo(String token);

    /**
     * @description 获取用户头像
     * @param: request
     * @param: response
     * @author hzq
     * @date 2023/2/9 00:29
     */
    void getAvatar(HttpServletRequest request, HttpServletResponse response);

    /**
     * @description 获取编译目录
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/9 07:40
     */
    Result<String> getCompilePath();

    /**
     * @description 获取用户代码执行记录
     * @param: topicId 题目id
     * @param pageNO 页码
     * @param languageType 语言类型
     * @Param pageSize 分页大小
     * @return: world.hzq.sjm.util.Result<world.hzq.sjm.entity.SolutionRecord>
     * @author hzq
     * @date 2023/2/10 01:07
     */
    Result<InfoVO<SolutionRecord>> getInvokeRecord(Long topicId, String languageType,Integer pageNO, Integer pageSize);

    /**
     * @description 获取执行记录状态类型
     * @return: world.hzq.sjm.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/10 02:13
     */
    Result<List<String>> getInvokeRecordStatusType();

}
