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
 * @date 2023/2/13 20:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("选择题信息传输实体")
public class AddChoiceTopicDTO {

    @ApiModelProperty("选择题id")
    private Long id;

    @ApiModelProperty("题目内容")
    private String content;

    @ApiModelProperty("题目答案")
    private String answer;

    @ApiModelProperty("截止时间")
    private Date stopTime;

    @ApiModelProperty("班级id")
    private Long classRoomId;

    @ApiModelProperty("教师id")
    private Long teacherId;
}
