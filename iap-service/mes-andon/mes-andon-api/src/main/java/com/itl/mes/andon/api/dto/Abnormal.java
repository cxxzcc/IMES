package com.itl.mes.andon.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yx
 * @date 2021/12/28
 * @since 1.8
 */
@Data
@ApiModel("异常")
public class Abnormal {
    @ApiModelProperty("安灯类型bo")
    private String andonTypeBo;
    @ApiModelProperty("安灯类型编号")
    private String andonType;
    @ApiModelProperty("安灯类型名称")
    private String andonTypeName;

    @ApiModelProperty("异常id集合")
    private List<String> typeAbnormals;
}
