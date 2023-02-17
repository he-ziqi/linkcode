package world.hzq.linkcode.service;

import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InfoVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 课堂信息管理服务
 * @date 2023/2/12 17:05
 */
public interface InfoManagementService {

    /**
     * @description 创建班级
     * @return: world.hzq.linkcode.util.Result<java.lang.String> 班级id
     * @author hzq
     * @date 2023/2/12 17:06
     */
    Result<String> createClassRoom(String name,String accouncement);

    /**
     * @description 创建课堂暗号
     * @param: classRoomId
     * @param: timeout
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/12 19:20
     */
    Result<String> generateInviteClassCode(Long classRoomId, Long timeout);

    /**
     * @description 通过暗号加入班级
     * @param: code
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/12 20:23
     */
    Result<String> joinClassRoomByCode(String code,String nickName);

    /**
     * @description 通过申请加入班级
     * @param: classRoomId
     * @param: nickName
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/12 21:17
     */
    Result<String> joinClassRoomByApply(Long classRoomId, String nickName);

    /**
     * @description 操作申请记录
     * @param: recordId
     * @param: operationType
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/13 00:17
     */
    Result<String> operationApplyRecord(Long recordId, String operationType,Long studentId,Long classRoomId,String nickName);

    /**
     * @description 获取申请记录操作类型
     * @return: world.hzq.linkcode.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/13 00:22
     */
    Result<List<String>> getApplyRecordOperationType();

    /**
     * @description 解散课堂
     * @param: classRoomId
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/13 01:06
     */
    Result<String> dissolveClassRoom(Long classRoomId);

    /**
     * @description 退出课堂
     * @param: classRoomId
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/13 01:38
     */
    Result<String> exitClassRoom(Long classRoomId);

    /**
     * @description 从课堂移除学生
     * @param: studentId
     * @param: classRoomId
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/13 01:47
     */
    Result<String> removeStudent(Long studentId, Long classRoomId);
}
