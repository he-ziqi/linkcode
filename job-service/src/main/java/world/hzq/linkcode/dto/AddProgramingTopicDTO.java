package world.hzq.linkcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/13 22:10
 */
@ApiModel("编程题作业信息传输实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProgramingTopicDTO {
    @ApiModelProperty("编程题作业id")
    private Long id;

    @ApiModelProperty("作业描述")
    private String description;

    @ApiModelProperty("截止时间")
    private Date stopTime;

    @ApiModelProperty("班级id")
    private Long classRoomId;

    @ApiModelProperty("教师id")
    private Long teacherId;

    @ApiModelProperty("编程题题目id")
    private Long topicId;
}
