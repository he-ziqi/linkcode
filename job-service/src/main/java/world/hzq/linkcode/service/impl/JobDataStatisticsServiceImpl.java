package world.hzq.linkcode.service.impl;

import org.springframework.stereotype.Service;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.mapper.JobDataStatisticsMapper;
import world.hzq.linkcode.service.JobDataStatisticsService;
import world.hzq.linkcode.type.JobCommitStatusType;
import world.hzq.linkcode.type.TopicType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.JobDataStatisticsVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 作业数据统计
 * @date 2023/2/16 16:29
 */
@Service
public class JobDataStatisticsServiceImpl implements JobDataStatisticsService {

    @Resource
    private JobDataStatisticsMapper jobDataStatisticsMapper;

    @Override
    public Result<JobDataStatisticsVO> choiceTopicInfoStatistics(Long classRoomId, Long choiceTopicId) {
        return Result.success("获取成功",getSubmitInfoLogic(classRoomId,choiceTopicId,TopicType.TOPIC_TYPE_CHOICE.getCode()));
    }

    @Override
    public Result<JobDataStatisticsVO> programTopicInfoStatistics(Long classRoomId, Long topicId) {
        return Result.success("获取成功",getSubmitInfoLogic(classRoomId,topicId,TopicType.TOPIC_TYPE_PROGRAM.getCode()));
    }

    private JobDataStatisticsVO getSubmitInfoLogic(Long classRoomId,Long topicId,String type){
        JobDataStatisticsVO resultVO = new JobDataStatisticsVO();
        //获取未通过的学生信息
        List<User> unPassStudentList = jobDataStatisticsMapper.getStudentBySubmitStatusAndAccepted(
                classRoomId,
                topicId,
                JobCommitStatusType.JOB_STATUS_COMMIT.getCode(),
                0, //未通过
                type
        );
        //获取已通过的学生信息
        List<User> passStudentList = jobDataStatisticsMapper.getStudentBySubmitStatusAndAccepted(
                classRoomId,
                topicId,
                JobCommitStatusType.JOB_STATUS_COMMIT.getCode(),
                1, //已通过
                type
        );
        //获取未提交的学生信息
        List<User> unCommitStudentList = jobDataStatisticsMapper.getStudentBySubmitStatusAndAccepted(
                classRoomId,
                topicId,
                JobCommitStatusType.JOB_STATUS_UNCOMMIT.getCode(),
                0, //未通过
                type
        );
        //获取各个学生在班级中的昵称
        unPassStudentList.forEach(unPassStudent -> {
            String nickName = jobDataStatisticsMapper.getStudentNickNameById(unPassStudent.getId(),classRoomId);
            unPassStudent.setNickName(nickName);
        });
        passStudentList.forEach(passStudent -> {
            String nickName = jobDataStatisticsMapper.getStudentNickNameById(passStudent.getId(),classRoomId);
            passStudent.setNickName(nickName);
        });
        unCommitStudentList.forEach(unCommitStudent -> {
            String nickName = jobDataStatisticsMapper.getStudentNickNameById(unCommitStudent.getId(),classRoomId);
            unCommitStudent.setNickName(nickName);
        });
        //计算班级总人数
        Integer totalNumber = unCommitStudentList.size() + passStudentList.size() + unPassStudentList.size();
        resultVO.setTotalNumber(totalNumber);
        //设置通过人数
        resultVO.setAcceptedNumber(passStudentList.size());
        //设置未提交人数
        resultVO.setUnSubmitNumber(unCommitStudentList.size());
        //设置未通过人数
        resultVO.setUnAcceptedNumber(unPassStudentList.size());
        //设置提交人数
        resultVO.setSubmitNumber(passStudentList.size() + unPassStudentList.size());
        //设置作业id
        resultVO.setJobId(topicId);
        //设置未通过学生信息
        resultVO.setUnAcceptedStudentInfo(unPassStudentList);
        //设置通过学生信息
        resultVO.setAcceptedStudentInfo(passStudentList);
        //设置未提交学生信息
        resultVO.setUnSubmitStudentInfo(unCommitStudentList);
        return resultVO;
    }
}
