package world.hzq.linkcode.service;

import world.hzq.linkcode.entity.Job;
import world.hzq.linkcode.util.Result;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/14 00:36
 */
public interface JobInfoService {
    /**
     * @description 获取作业信息(无条件)
     * @param: pageNo
     * @param: pageSize
     * @return: world.hzq.linkcode.util.Result<world.hzq.linkcode.vo.InfoVO<world.hzq.linkcode.entity.Job>>
     * @author hzq
     * @date 2023/2/14 00:40
     */
    Result<Job> getJobInfo(Long classRoomId,Integer pageNo, Integer pageSize);

    /**
     * @description 获取作业类型
     * @return: world.hzq.linkcode.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/14 16:04
     */
    Result<List<String>> getJobType();

    /**
     * @description 获取作业状态类型
     * @return: world.hzq.linkcode.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/14 16:05
     */
    Result<List<String>> getJobStatusType();

}
