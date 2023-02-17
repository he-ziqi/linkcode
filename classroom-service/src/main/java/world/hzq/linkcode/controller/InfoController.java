package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.service.InfoService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.ApplyRecordVO;
import world.hzq.linkcode.vo.ClassStudentVO;
import world.hzq.linkcode.vo.InfoVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 信息获取
 * @date 2023/2/12 23:38
 */
@RestController
@RequestMapping("/info")
@Api(tags = "课堂信息获取")
public class InfoController {
    @Autowired
    private InfoService infoService;

    @ApiOperation("获取申请记录")
    @GetMapping("/get/apply/record/{pageNo}/{pageSize}")
    public Result<InfoVO<ApplyRecordVO>> getApplyRecord(@ApiParam("页码") @PathVariable("pageNo") Integer pageNo,
                                                        @ApiParam("分页大小") @PathVariable("pageSize") Integer pageSize){
        return infoService.getApplyRecord(pageNo,pageSize);
    }

    @ApiOperation("获取班级信息(无条件)")
    @GetMapping("/get/class/room/{pageNo}/{pageSize}")
    public Result<InfoVO<ClassRoom>> getClassRoomInfo(@ApiParam("页码") @PathVariable("pageNo") Integer pageNo,
                                                      @ApiParam("分页大小") @PathVariable("pageSize") Integer pageSize){
        return infoService.getClassRoom(pageNo,pageSize);
    }

    @ApiOperation("获取班级信息(有条件)")
    @GetMapping("/get/class/room/condition/{pageNo}/{pageSize}")
    public Result<InfoVO<ClassRoom>> getClassRoomInfoByCondition(@ApiParam("班级名称") @RequestParam(value = "className",required = false) String className,
                                                                 @ApiParam("创建人昵称") @RequestParam(value = "creator",required = false) String creator,
                                                                 @ApiParam("页码") @PathVariable("pageNo") Integer pageNo,
                                                                 @ApiParam("分页大小") @PathVariable("pageSize") Integer pageSize){
        return infoService.getClassRoomByCondition(className,creator,pageNo,pageSize);
    }


    @ApiOperation("获取课堂的学生列表信息")
    @GetMapping("/get/class/student/list/{classRoomId}/{pageNo}/{pageSize}")
    public Result<InfoVO<ClassStudentVO>> getClassRoomStudent(@ApiParam("课堂id") @PathVariable("classRoomId") Long classRoomId,
                                                              @ApiParam("页码") @PathVariable("pageNo") Integer pageNo,
                                                              @ApiParam("页面大小") @PathVariable("pageSize") Integer pageSize){
        return infoService.getClassRoomStudent(classRoomId,pageNo,pageSize);
    }

    @ApiOperation("获取申请状态类型")
    @GetMapping("/get/apply/status/type")
    public Result<List<String>> getApplyStatusType(){
        return infoService.getApplyStatusType();
    }

}
