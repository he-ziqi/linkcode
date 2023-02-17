package world.hzq.linkcode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.mapper.ClassRoomInfoManagementMapper;
import world.hzq.linkcode.service.InfoManagementService;
import world.hzq.linkcode.type.ApplyRecordOperationType;
import world.hzq.linkcode.type.RoleType;
import world.hzq.linkcode.util.*;
import world.hzq.linkcode.vo.ApplyRecordVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 17:06
 */
@Service
public class InfoManagementServiceImpl implements InfoManagementService {

    @Resource
    private ClassRoomInfoManagementMapper classRoomInfoManagementMapper;

    @Autowired
    private WebUtil webUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createClassRoom(String name,String accouncement) {
        User user = webUtil.currentUser();
        //不是教师
        if(!Tools.equals(user.getRoleType(), RoleType.ROLE_NAME_TEACHER.getCode())){
            return Result.fail("非法操作,权限不足");
        }
        ClassRoom classRoom = new ClassRoom();
        classRoom.setCreateTime(new Date());
        classRoom.setName(name);
        classRoom.setAccouncement(accouncement);
        classRoom.setNumber(0);
        classRoomInfoManagementMapper.createClassRoom(classRoom);
        Integer inserted = classRoomInfoManagementMapper.addClassRoomTeacherRelation(classRoom.getId(),user.getId());
        return classRoom.getId() > 0 && inserted == 1 ? Result.success("创建成功") : Result.fail("创建失败");
    }



    @Override
    public Result<String> generateInviteClassCode(Long classRoomId, Long timeout) {
        //检查登录者身份
        User user = webUtil.currentUser();
        if(!Tools.equals(user.getRoleType(),RoleType.ROLE_NAME_TEACHER.getCode())){
            return Result.fail("非法请求,权限不足");
        }
        //检查课堂是否存在
        ClassRoom classRoom = classRoomInfoManagementMapper.getClassRoomByTeacherIdAndClassRoomId(classRoomId,user.getId());
        if(classRoom == null){
            return Result.fail("班级不存在或您不是班级创建人");
        }
        String uuid = UUIDGenerator.getUUID();
        //存入redis 并设置超时
        stringRedisTemplate.opsForValue().set(uuid,String.valueOf(classRoom.getId()),timeout, TimeUnit.SECONDS);
        return Result.success("暗号创建成功",uuid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> joinClassRoomByCode(String code,String nickName) {
        if(Tools.isNull(code)){
            return Result.fail("暗号不能为空哦");
        }
        if(Tools.isNull(nickName)){
            return Result.fail("昵称不能为空哦");
        }
        User user = webUtil.currentUser();
        if(Tools.equals(user.getRoleType(),RoleType.ROLE_NAME_TEACHER.getCode())){
            return Result.fail("教师不能加入班级哦");
        }
        String classRoomIdStr = stringRedisTemplate.opsForValue().get(code);
        if(Tools.isNull(classRoomIdStr)){
            return Result.fail("暗号不正确或暗号已过期");
        }
        Long classRoomId = Long.parseLong(classRoomIdStr);
        //加入班级
        Integer inserted = classRoomInfoManagementMapper.joinClassRoom(classRoomId,user.getId(),new Date(),nickName);
        //更新此班级人数+1
        Integer updated = classRoomInfoManagementMapper.updateClassRoomNumberIncrement(classRoomId,1);
        return inserted == 1 && updated == 1 ? Result.success("班级加入成功") : Result.fail("加入班级失败");
    }

    @Override
    public Result<String> joinClassRoomByApply(Long classRoomId, String nickName) {
        if(Tools.isNull(nickName)){
            return Result.fail("昵称不能为空哦");
        }
        User user = webUtil.currentUser();
        if(Tools.equals(user.getRoleType(),RoleType.ROLE_NAME_TEACHER.getCode())){
            return Result.fail("教师不能加入班级哦");
        }
        ClassRoom classRoom = classRoomInfoManagementMapper.getClassRoomById(classRoomId);
        if(classRoom != null){
            Result.fail("加入班级失败,班级不存在");
        }
        //通过班级id找到教师id
        Long teacherId = classRoomInfoManagementMapper.getTeacherIdByClassRoomId(classRoomId);
        //添加申请记录 7天后过期
        Date now = new Date();
        Integer inserted = classRoomInfoManagementMapper.addApplyRecord(now, TimeUtil.afterTime(now,7),classRoomId,teacherId,user.getId(),nickName);
        return inserted == 1 ? Result.success("申请成功") : Result.fail("申请失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> operationApplyRecord(Long recordId, String operationType,Long studentId,Long classRoomId,String nickName) {
        User user = webUtil.currentUser();
        if (!Tools.equals(user.getRoleType(), RoleType.ROLE_NAME_TEACHER.getCode())) {
            return Result.fail("您无权限操作");
        }
        //验证当前用户是否是班级创建者
        ApplyRecordVO applyRecordVO = classRoomInfoManagementMapper.getApplyRecordById(recordId);
        if(!applyRecordVO.getTeacher().getId().equals(user.getId())){
            return Result.fail("您不是创建者,无权操作哦");
        }
        if (Tools.equals(applyRecordVO.getApplyStatus(),operationType)) {
            return Result.fail("已经是这个状态了,不能重复操作哦");
        }
        //过期时间小于当前时间 已过期 不可操作
        if(!TimeUtil.thanTime(applyRecordVO.getExpirationTime(),new Date())){
            return Result.fail("当前记录已过期,不可操作");
        }
        Integer updatedRecord = classRoomInfoManagementMapper.updateApplyRecord(recordId,operationType);
        //如果是申请通过 则将学生加入到班级中
        if(Tools.equals(operationType,ApplyRecordOperationType.APPLY_OPERATION_PASS_TYPE.getCode())){
            //加入班级
            Integer inserted = classRoomInfoManagementMapper.joinClassRoom(classRoomId,studentId,new Date(),nickName);
            //更新此班级人数+1
            Integer updatedNumber = classRoomInfoManagementMapper.updateClassRoomNumberIncrement(classRoomId,1);
            return updatedRecord == 1 && updatedNumber == 1 && inserted == 1
                    ? Result.success("操作成功") : Result.fail("操作失败");
        }
        return updatedRecord == 1 ? Result.success("操作成功") : Result.fail("操作失败");
    }

    @Override
    public Result<List<String>> getApplyRecordOperationType() {
        List<String> types = new ArrayList<>(ApplyRecordOperationType.values().length);
        for (ApplyRecordOperationType type : ApplyRecordOperationType.values()) {
            types.add(type.getCode());
        }
        return Result.success(types);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> dissolveClassRoom(Long classRoomId) {
        //解散课堂
        //1、身份验证
        User user = webUtil.currentUser();
        if(!Tools.equals(user.getRoleType(),RoleType.ROLE_NAME_TEACHER.getCode())){
            return Result.fail("非法操作,权限不足");
        }
        //2、课堂存在验证
        //根据教师id和班级id获取班级
        ClassRoom room = classRoomInfoManagementMapper.getClassRoomByTeacherIdAndClassRoomId(classRoomId, user.getId());
        if(Tools.isNull(room)){
            return Result.fail("只能删除自己创建的班级哦");
        }
        //3、删除课堂学生关系记录(可能无记录)
        Integer deleted1 = classRoomInfoManagementMapper.deleteClassRoomStudentRelationByClassRoomId(classRoomId);
        //4、删除课堂教师关系记录
        Integer deleted2 = classRoomInfoManagementMapper.deleteClassRoomTeacherRelationByClassRoomId(classRoomId);
        //5、删除申请记录(可能无记录)
        Integer deleted3 = classRoomInfoManagementMapper.deleteClassRoomApplyRecordByClassRoomId(classRoomId);
        //6、删除课堂记录
        Integer deleted4 = classRoomInfoManagementMapper.deleteClassRoomById(classRoomId);
        return deleted1 + deleted2 + deleted3 + deleted4 >= 2 ? Result.success("解散成功") : Result.fail("解散失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> exitClassRoom(Long classRoomId) {
        //1、删除申请记录(可能无记录)
        Integer deleted1 = classRoomInfoManagementMapper.deleteClassRoomApplyRecordByClassRoomId(classRoomId);
        //2、更新班级人数-1
        Integer updated = classRoomInfoManagementMapper.updateClassRoomNumberIncrement(classRoomId, -1);
        //3、删除课堂学生关系记录
        Integer deleted2 = classRoomInfoManagementMapper.deleteClassRoomStudentRelationByClassRoomId(classRoomId);
        return updated + deleted1 + deleted2 >= 2 ? Result.success("退出成功") : Result.fail("退出失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> removeStudent(Long studentId, Long classRoomId) {
        //1、身份验证
        User user = webUtil.currentUser();
        if(!Tools.equals(user.getRoleType(),RoleType.ROLE_NAME_TEACHER.getCode())){
            return Result.fail("您无权限哦");
        }
        ClassRoom classRoom = classRoomInfoManagementMapper.getClassRoomByTeacherIdAndClassRoomId(classRoomId, user.getId());
        if(Tools.isNull(classRoom)){
            return Result.fail("您无权限操作");
        }
        //2、删除申请记录(可能无记录)
        Integer deleted1 = classRoomInfoManagementMapper.deleteClassRoomApplyRecordByClassRoomIdAndStudentId(studentId,classRoomId);
        //3、更新班级人数-1
        Integer updated = classRoomInfoManagementMapper.updateClassRoomNumberIncrement(classRoomId, -1);
        //4、删除课堂学生关系记录
        Integer deleted2 = classRoomInfoManagementMapper.deleteClassRoomStudentRelationByClassRoomIdAndStudentId(studentId,classRoomId);
        return updated + deleted1 + deleted2 >= 2 ? Result.success("移除成功") : Result.fail("移除失败");
    }

}
