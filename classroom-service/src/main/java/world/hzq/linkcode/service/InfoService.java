package world.hzq.linkcode.service;

import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.ApplyRecordVO;
import world.hzq.linkcode.vo.ClassStudentVO;
import world.hzq.linkcode.vo.InfoVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 信息获取服务
 * @date 2023/2/12 23:39
 */
public interface InfoService {
    /**
     * @description 获取申请记录
     * @param: pageNo
     * @param: pageSize
     * @return: world.hzq.linkcode.util.Result<java.util.List<world.hzq.linkcode.vo.ApplyRecordVO>>
     * @author hzq
     * @date 2023/2/12 21:58
     */
    Result<InfoVO<ApplyRecordVO>> getApplyRecord(Integer pageNo, Integer pageSize);

    /**
     * @description 获取课堂的学生列表
     * @param: classRoomId
     * @param: pageNo
     * @param: pageSize
     * @return: world.hzq.linkcode.util.Result<world.hzq.linkcode.vo.InfoVO<world.hzq.linkcode.entity.User>>
     * @author hzq
     * @date 2023/2/12 23:47
     */
    Result<InfoVO<ClassStudentVO>> getClassRoomStudent(Long classRoomId, Integer pageNo, Integer pageSize);

    /**
     * @description 获取班级信息
     * @param: pageNo
     * @param: pageSize
     * @return: world.hzq.linkcode.util.Result<java.util.List<world.hzq.linkcode.entity.ClassRoom>>
     * @author hzq
     * @date 2023/2/12 17:42
     */
    Result<InfoVO<ClassRoom>> getClassRoom(Integer pageNo, Integer pageSize);

    /**
     * @description 获取班级信息 有条件
     * @param: className
     * @param: creator
     * @return: world.hzq.linkcode.util.Result<world.hzq.linkcode.vo.InfoVO<world.hzq.linkcode.entity.ClassRoom>>
     * @author hzq
     * @date 2023/2/13 14:33
     */
    Result<InfoVO<ClassRoom>> getClassRoomByCondition(String className, String creator,Integer pageNo,Integer pageSize);

    /**
     * @description 获取申请状态类型
     * @return: world.hzq.linkcode.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/13 15:26
     */
    Result<List<String>> getApplyStatusType();

}
