package world.hzq.linkcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 题目信息展示
 * @date 2023/2/5 15:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("查询信息展示实体")
public class InfoVO <T>{
    @ApiModelProperty("查询主题信息列表")
    private List<T> source;
    @ApiModelProperty("记录总数")
    private Long total;
}
