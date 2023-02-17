package world.hzq.linkcode.service;

import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.JobDataStatisticsVO;

/**
 * @author hzq
 * @version 1.0
 * @description 选择题数据统计
 * @date 2023/2/16 16:28
 */
public interface JobDataStatisticsService {
    /**
     * @description 获取班级的选择题作业数据统计信息
     * @param: classRoomId
     * @return: world.hzq.linkcode.util.Result<world.hzq.linkcode.vo.ChoiceTopicDataStatisticsVO>
     * @author hzq
     * @date 2023/2/16 16:43
     */
    Result<JobDataStatisticsVO> choiceTopicInfoStatistics(Long classRoomId, Long choiceTopicId);

    /**
     * @description 编程题数据统计
     * @param: classRoomId
     * @param: topicId
     * @return: world.hzq.linkcode.util.Result<world.hzq.linkcode.vo.JobDataStatisticsVO>
     * @author hzq
     * @date 2023/2/16 23:02
     */
    Result<JobDataStatisticsVO> programTopicInfoStatistics(Long classRoomId, Long topicId);
}
