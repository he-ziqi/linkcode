package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import world.hzq.linkcode.entity.User;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 选择题数据统计VO
 * @date 2023/2/16 16:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("作业数据统计展示实体")
public class JobDataStatisticsVO {
    @ApiModelProperty("作业id")
    private Long jobId;

    @ApiModelProperty("提交人数")
    private Integer submitNumber;

    @ApiModelProperty("班级总人数")
    private Integer totalNumber;

    @ApiModelProperty("通过人数")
    private Integer acceptedNumber;

    @ApiModelProperty("未通过人数")
    private Integer unAcceptedNumber;

    @ApiModelProperty("未提交人数")
    private Integer unSubmitNumber;

    @ApiModelProperty("未通过学生信息")
    private List<User> unAcceptedStudentInfo;

    @ApiModelProperty("通过学生信息")
    private List<User> acceptedStudentInfo;

    @ApiModelProperty("未提交学生信息")
    private List<User> unSubmitStudentInfo;

    @ApiModelProperty("作业类型")
    private String type;

}
