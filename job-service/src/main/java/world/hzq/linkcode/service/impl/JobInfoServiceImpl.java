package world.hzq.linkcode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.hzq.linkcode.entity.*;
import world.hzq.linkcode.mapper.JobInfoMapper;
import world.hzq.linkcode.service.JobInfoService;
import world.hzq.linkcode.type.JobCommitStatusType;
import world.hzq.linkcode.type.RoleType;
import world.hzq.linkcode.type.TopicType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;
import world.hzq.linkcode.util.WebUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 作业信息服务实现
 * @date 2023/2/14 00:37
 */
@Service
public class JobInfoServiceImpl implements JobInfoService {
    @Resource
    private JobInfoMapper jobInfoMapper;

    @Autowired
    private WebUtil webUtil;

    @Override
    public Result<Job> getJobInfo(Long classRoomId,Integer pageNo, Integer pageSize) {
        User user = webUtil.currentUser();
        Job job = null;
        //是教师
        if(Tools.equals(user.getRoleType(), RoleType.ROLE_NAME_TEACHER.getCode())){
            job = getJobInfoLogic(user.getId(),false,null,classRoomId,pageNo,pageSize);
        }else{
            //是学生
            //通过班级id获取到该班级的教师id
            Long teacherId = jobInfoMapper.getTeacherByClassRoomId(classRoomId);
            //执行获取作业信息的逻辑即可
            job = getJobInfoLogic(teacherId,true,user.getId(),classRoomId,pageNo,pageSize);
        }
        return Result.success("获取成功",job);
    }

    @Override
    public Result<List<String>> getJobType() {
        List<String> types = new ArrayList<>(TopicType.values().length);
        for (TopicType type : TopicType.values()) {
            types.add(type.getCode());
        }
        return Result.success("获取成功",types);
    }

    @Override
    public Result<List<String>> getJobStatusType() {
        List<String> types = new ArrayList<>(JobCommitStatusType.values().length);
        for (JobCommitStatusType type : JobCommitStatusType.values()) {
            types.add(type.getCode());
        }
        return Result.success("获取成功",types);
    }

    //获取作业信息的逻辑
    @Transactional(rollbackFor = Exception.class)
    public Job getJobInfoLogic(Long teacherId,boolean isStudent,Long studentId,Long classRoomId,Integer pageNo,Integer pageSize){
        Job job = new Job();
        List<ChoiceTopic> choiceTopicList = null;
        List<ProgramTopic> programTopicList = null;
        //获取该教师在该课堂发布的选择题作业数量
        Long choiceTopicCount = jobInfoMapper.getChoiceJobInfoCount(teacherId,classRoomId);
        //获取编程题作业数量
        Long programTopicCount = jobInfoMapper.getProgramJobInfoCount(teacherId,classRoomId);
        //获取教师信息
        User teacher = jobInfoMapper.getTeacherById(teacherId);
        //获取班级信息
        ClassRoom classRoom = jobInfoMapper.getClassRoomById(classRoomId);
        //获取该教师在该课堂布置的选择题
        choiceTopicList = jobInfoMapper.getChoiceJobByTeacherIdAndClassRoomId(teacherId,classRoomId,(pageNo - 1) * pageSize,pageSize);
        //获取编程题
        programTopicList = jobInfoMapper.getProgramJobByTeacherIdAndClassRoomId(teacherId,classRoomId,(pageNo - 1) * pageSize,pageSize);
        //如果已超时则更新选择题题目状态
        jobInfoMapper.updateChoiceTopicStatus(JobCommitStatusType.JOB_STATUS_TIMEOUT.getCode());
        //更新编程题题目状态
        jobInfoMapper.updateProgramTopicStatus(JobCommitStatusType.JOB_STATUS_TIMEOUT.getCode());
        if(isStudent){ //是学生,需要多查询一个状态
            choiceTopicList.forEach(choiceTopic -> {
                //根据学生id、选择题id、班级id查询该学生的选择题提交状态
                String status = jobInfoMapper.getTopicCommitStatus(choiceTopic.getId(), TopicType.TOPIC_TYPE_CHOICE.getCode(), studentId,classRoomId);
                choiceTopic.setStatus(status);
            });
            programTopicList.forEach(
                programTopic -> {
                    //查询编程题的状态并设置
                    String status = jobInfoMapper.getTopicCommitStatus(programTopic.getId(), TopicType.TOPIC_TYPE_PROGRAM.getCode(), studentId,classRoomId);
                    programTopic.setStatus(status);
                }
            );
        }
        job.setTeacher(teacher);
        job.setClassRoom(classRoom);
        job.setChoiceTopicList(choiceTopicList);
        job.setProgramTopicList(programTopicList);
        job.setChoiceTopicTotal(choiceTopicCount);
        job.setProgramTopicTotal(programTopicCount);
        return job;
    }

}
