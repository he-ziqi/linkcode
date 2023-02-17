package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.service.InfoManagementService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InfoVO;

import java.util.List;


/**
 * @author hzq
 * @version 1.0
 * @description 课堂信息管理
 * @date 2023/2/12 14:40
 */
@RestController
@RequestMapping("/manage")
@Api(tags = "课堂信息管理")
public class InfoManagementController {

    @Autowired
    private InfoManagementService infoManagementService;

    @ApiOperation("创建课堂")
    @PostMapping("/create/class/room")
    public Result<String> createClassRoom(@ApiParam("班级名称") @RequestParam(value = "name") String name,
                                          @ApiParam("班级公告") @RequestParam(value = "accouncement") String accouncement){
        return infoManagementService.createClassRoom(name,accouncement);
    }

    @ApiOperation("生成课堂暗号")
    @GetMapping("/generate/code/class")
    public Result<String> generateInviteClassCode(@ApiParam("班级id") @RequestParam("classRoomId") Long classRoomId,
                                                  @ApiParam("过期时间(单位：秒)") @RequestParam(value = "timeout",required = false) Long timeout){
        if(timeout == null){ //默认过期时间是7天
            timeout = 3600 * 24 * 7L;
        }
        return infoManagementService.generateInviteClassCode(classRoomId,timeout);
    }

    @ApiOperation("通过暗号加入班级")
    @PostMapping("/join/class/room/code")
    public Result<String> joinClassRoomByCode(@ApiParam("暗号") @RequestParam("code") String code,
                                              @ApiParam("班级内昵称") @RequestParam("nickName") String nickName){
        return infoManagementService.joinClassRoomByCode(code,nickName);
    }

    @ApiOperation("通过申请加入班级")
    @PostMapping("/join/class/room/apply")
    public Result<String> joinClassRoomByApply(@ApiParam("班级id") @RequestParam("classRoomId") Long classRoomId,
                                               @ApiParam("班级内昵称") @RequestParam("nickName") String nickName){
        return infoManagementService.joinClassRoomByApply(classRoomId,nickName);
    }

    @ApiOperation("操作申请记录")
    @PostMapping("/operation/{recordId}/{studentId}/{classRoomId}")
    public Result<String> operationApplyRecord(@ApiParam("操作类型") @RequestParam("operationType") String operationType,
                                               @ApiParam("学生在班级中的昵称") @RequestParam("nickName") String nickName,
                                               @ApiParam("记录id") @PathVariable("recordId") Long recordId,
                                               @ApiParam("学生id") @PathVariable("studentId") Long studentId,
                                               @ApiParam("班级id") @PathVariable("classRoomId") Long classRoomId){
        return infoManagementService.operationApplyRecord(recordId,operationType,studentId,classRoomId,nickName);
    }

    @ApiOperation("获取申请记录操作类型")
    @GetMapping("/get/apply/record/operation/type")
    public Result<List<String>> getOperationType(){
        return infoManagementService.getApplyRecordOperationType();
    }

    @ApiOperation("解散课堂")
    @DeleteMapping("/dissolve/class/room/{classRoomId}")
    public Result<String> dissolveClassRoom(@ApiParam("课堂id") @PathVariable("classRoomId") Long classRoomId){
        return infoManagementService.dissolveClassRoom(classRoomId);
    }

    @ApiOperation("退出课堂")
    @DeleteMapping("/exit/class/room/{classRoomId}")
    public Result<String> exitClassRoom(@ApiParam("课堂id") @PathVariable("classRoomId") Long classRoomId){
        return infoManagementService.exitClassRoom(classRoomId);
    }

    @ApiOperation("从课堂移除学生")
    @DeleteMapping("/remove/student/{classRoomId}/{studentId}")
    public Result<String> removeStudent(@ApiParam("学生id") @PathVariable("studentId") Long studentId,
                                        @ApiParam("班级id") @PathVariable("classRoomId") Long classRoomId){
        return infoManagementService.removeStudent(studentId,classRoomId);
    }

}
