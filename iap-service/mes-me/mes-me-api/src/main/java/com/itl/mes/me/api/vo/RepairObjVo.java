package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "SendRepairObjVo", description = "维修工位 维修详情数据展示 与 WipLog的BO")
public class RepairObjVo implements Serializable {

    @ApiModelProperty(value = "维修详情")
    private List<RepairLogListVo> repairLogListVo;

    @ApiModelProperty(value = "WipLog的BO")
    private String wipLogBo;
}
