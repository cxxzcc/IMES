package com.itl.mes.andon.api.vo;

import com.itl.mes.andon.api.dto.Abnormal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/25
 */
@Data
@ApiModel(value = "AndonLookPlateVo",description = "安灯看板返回实体")
public class AndonLookPlateVo {



    @ApiModelProperty(value = "资源名称")
    private String resourceName;


    @ApiModelProperty(value = "是否异常,true表示异常，false表示正常")
    private Boolean isAbnormal;

    @ApiModelProperty("异常集合")
    private List<Abnormal> abnormalList;
}
