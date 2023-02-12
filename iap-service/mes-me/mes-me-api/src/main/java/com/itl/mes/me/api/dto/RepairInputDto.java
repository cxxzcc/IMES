package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("维修录入")
public class RepairInputDto implements Serializable {

    /**
     * 每条送修详情对应的MeSfcNcLog的BO
     */
    @ApiModelProperty(value = "每条送修详情对应的MeSfcNcLog的BO",required = true)
    private String ngLogBo;

    /**
     * sfc表的sfc字段,其实就是成品SN
     */
    @ApiModelProperty(value = "sfc表的sfc字段",required = true)
    private String sfc;

    /**
     * 维修原因(字典维护)
     */
    @ApiModelProperty(value = "维修原因(字典维护)")
    private String repairReason;

    /**
     * 维修方式（字典维护)
     */
    @ApiModelProperty(value = "维修方式（字典维护)")
    private String repairMethod;

    /**
     * 责任单位
     */
    @ApiModelProperty(value = "责任单位")
    private String dutyUnit;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 替换组件SN
     */
    @ApiModelProperty(value = "替换组件SN")
    private String replaceItemSn;

    /**
     * 不良组件SN
     */
    @ApiModelProperty(value = "不良组件SN")
    private String badItemSn;

    /**
     * 可能用不到
     * 不良组件的ItemBO 与 MeSfcNcLog表的componentBo对应
     */
    @ApiModelProperty(value = "不良组件的ItemBO",required = false)
    private String badItemBo;
}
