package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "RepairDetailsVo", description = "维修工位 送修详情数据展示")
public class SendRepairDetailsVo implements Serializable {

/*
    @ApiModelProperty(value = "不良代码BO")
    private String ncCodeBo;
*/

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良描述")
    private String ncDesc;

    /**
     * 送修详情的每条数据和NGLog表一对一,后续插入repair表也只能是NGLog表中的损坏记录
     */
    @ApiModelProperty(value = "不良物料编码:组件的物料编码")
    private String item;
    @ApiModelProperty(value = "MeSfcNcLog的BO")
    private String ngLogBo;

    @ApiModelProperty(value = "不良物料的BO")
    private String badItemBo;
    //badItemBo可能用不到

    @ApiModelProperty(value = "不良位置")
    private String componentPosition;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 是否原材料检测,me_sfc_nc_log表里的维修状态,修好就是1,没修好是0
     */
    @ApiModelProperty(value = "是否原材料检测")
    private Boolean isRawCheck;
}
