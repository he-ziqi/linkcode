package world.hzq.linkcode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.mapper.ClassRoomInfoMapper;
import world.hzq.linkcode.service.InfoService;
import world.hzq.linkcode.type.ApplyStatusType;
import world.hzq.linkcode.type.RoleType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;
import world.hzq.linkcode.util.WebUtil;
import world.hzq.linkcode.vo.ApplyRecordVO;
import world.hzq.linkcode.vo.ClassStudentVO;
import world.hzq.linkcode.vo.InfoVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 23:40
 */
@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    private WebUtil webUtil;

    @Resource
    private ClassRoomInfoMapper classRoomInfoMapper;


    @Override
    public Result<InfoVO<ApplyRecordVO>> getApplyRecord(Integer pageNo, Integer pageSize) {
        User user = webUtil.currentUser();
        List<ApplyRecordVO> applyRecordVOList = null;
        Long count = null;
        //获取的是学生
        if(Tools.equals(user.getRoleType(), RoleType.ROLE_NAME_STUDENT.getCode())){
            count = classRoomInfoMapper.getApplyRecordCountByStudentId(user.getId());
            applyRecordVOList = classRoomInfoMapper.getApplyRecordByStudentId(user.getId(),(pageNo - 1) * pageSize,pageSize);
        }else{ //获取的是老师
            count = classRoomInfoMapper.getApplyRecordCountByTeacherId(user.getId());
            applyRecordVOList = classRoomInfoMapper.getApplyRecordByTeacherId(user.getId(),(pageNo - 1) * pageSize,pageSize);
        }
        InfoVO<ApplyRecordVO> infoVO = new InfoVO<>();
        infoVO.setTotal(count);
        applyRecordVOList.forEach(applyRecordVO -> {
            User applyStudent = applyRecordVO.getApplyStudent();
            User teacher = applyRecordVO.getTeacher();
            //信息脱敏
            applyStudent.setPhoneNumber(Tools.phoneNumberChange(applyStudent.getPhoneNumber()));
            applyStudent.setEmail(Tools.emailChange(applyStudent.getEmail()));
            applyStudent.setRoleType(RoleType.ROLE_NAME_STUDENT.getCode());
            teacher.setPhoneNumber(Tools.phoneNumberChange(teacher.getPhoneNumber()));
            teacher.setEmail(Tools.emailChange(teacher.getEmail()));
            teacher.setRoleType(RoleType.ROLE_NAME_TEACHER.getCode());
        });
        infoVO.setSource(applyRecordVOList);
        return Result.success("获取成功",infoVO);
    }

    @Override
    public Result<InfoVO<ClassStudentVO>> getClassRoomStudent(Long classRoomId, Integer pageNo, Integer pageSize) {
        InfoVO<ClassStudentVO> infoVO = new InfoVO<>();
        Long count = classRoomInfoMapper.getClassRoomStudentCount(classRoomId);
        List<ClassStudentVO> studentList = classRoomInfoMapper.getClassRoomStudentList(classRoomId,(pageNo - 1) * pageSize,pageSize);
        studentList.forEach(classStudentVO -> classStudentVO.getStudent().setRoleType(RoleType.ROLE_NAME_STUDENT.getCode()));
        infoVO.setTotal(count);
        infoVO.setSource(studentList);
        return Result.success("获取成功",infoVO);
    }

    @Override
    public Result<InfoVO<ClassRoom>> getClassRoom(Integer pageNo, Integer pageSize) {
        return getClassRoomLogic(null,null,pageNo,pageSize,"noCondition");
    }

    @Override
    public Result<InfoVO<ClassRoom>> getClassRoomByCondition(String className, String creator,Integer pageNo,Integer pageSize) {
        return getClassRoomLogic(className,creator,pageNo,pageSize,"condition");
    }

    @Override
    public Result<List<String>> getApplyStatusType() {
        List<String> types = new ArrayList<>(ApplyStatusType.values().length);
        for (ApplyStatusType type : ApplyStatusType.values()) {
            types.add(type.getCode());
        }
        return Result.success(types);
    }

    private Result<InfoVO<ClassRoom>> getClassRoomLogic(String className,String creator,Integer pageNo,Integer pageSize,String condition){
        User user = webUtil.currentUser();
        InfoVO<ClassRoom> infoVO = new InfoVO<>();
        Long count = null;
        List<ClassRoom> classRoomList = null;
        //教师身份
        if(Tools.equals(user.getRoleType(),RoleType.ROLE_NAME_TEACHER.getCode())){
            //获取该教师创建的班级总数
            if(Tools.isNull(className) && Tools.isNull(creator)){
                //无条件搜索 直接获取总数即可
                count = classRoomInfoMapper.getClassRoomCountByTeacherId(user.getId());
                //分页获取班级信息
                classRoomList = classRoomInfoMapper.getClassRoomByTeacherId(user.getId(),(pageNo - 1) * pageSize,pageSize);
            }else {
                //有条件搜索 通过条件+id获取总数
                count = classRoomInfoMapper.getCLassRoomCountByTeacherIdAndClassName(user.getId(),className);
                //教师搜索自己创建的班级
                classRoomList = classRoomInfoMapper.getClassRoomByTeacherIdAndClassName(user.getId(),className,(pageNo - 1) * pageSize,pageSize);
            }
        }else if(Tools.equals(user.getRoleType(),RoleType.ROLE_NAME_STUDENT.getCode())){ //学生身份
            // 无条件搜索时展示该学生已加入的班级信息
            if(Tools.isNull(className) && Tools.isNull(creator) && "noCondition".equals(condition)) {
                //获取该学生加入的班级总数
                count = classRoomInfoMapper.getClassRoomCountByStudentId(user.getId());
                classRoomList = classRoomInfoMapper.getCLassRoomByStudentId(user.getId(), (pageNo - 1) * pageSize, pageSize);
            }else {
                //学生搜索自己未加入的班级 通过条件+id获取总数
                count = classRoomInfoMapper.getClassRoomStudentNotJoinCountByCondition(user.getId(),className,creator);
                classRoomList = classRoomInfoMapper.getClassRoomStudentNotJoin(user.getId(), className,creator,(pageNo - 1) * pageSize, pageSize);
            }
        }
        if(Tools.isNull(classRoomList)){
            infoVO.setTotal(0L);
            return Result.success(infoVO);
        }
        //获取创建班级的教师
        classRoomList.forEach(classRoom -> {
            Long classRoomId = classRoom.getId();
            User teacher = classRoomInfoMapper.getTeacherByClassRoomId(classRoomId);
            //信息脱敏
            teacher.setPhoneNumber(Tools.phoneNumberChange(teacher.getPhoneNumber()));
            teacher.setEmail(Tools.emailChange(teacher.getEmail()));
            teacher.setRoleType(RoleType.ROLE_NAME_TEACHER.getCode());
            classRoom.setTeacher(teacher);
        });
        infoVO.setTotal(count);
        infoVO.setSource(classRoomList);
        return Result.success("获取成功",infoVO);
    }
}
