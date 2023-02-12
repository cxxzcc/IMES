package com.itl.iap.mes.api.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 设备备件更换记录
 * @author dengou
 * @date 2021/9/28
 */
@Data
public class SparePartChangeRecordDTO {

    /**
     * 备件id
     * */
    @ApiModelProperty(value="备件id", hidden = true)
    private String sparePartId;

    /**
     * 备件出库单
     * */
    @ApiModelProperty(value="备件出库单编号")
    private String orderNo;
    /**
     * 备件名称
     * */
    @ApiModelProperty(value="备件名称")
    private String name;
    /**
     * 领用日期
     * */
    @ApiModelProperty(value="领用日期")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date recordTime;
    /**
     * 领用人
     * */
    @ApiModelProperty(value="领用人")
    private String agent;

    /**
     * 备件类型
     * */
    @ApiModelProperty(value="备件类型")
    private String type;
    /**
     * 备件类型说明
     * */
    @ApiModelProperty(value="备件类型说明")
    private String typeDesc;
    /**
     * 规格型号
     * */
    @ApiModelProperty(value="规格型号")
    private String spec;
    /**
     * 使用数量
     * */
    @ApiModelProperty(value="使用数量")
    private String count;
    /**
     * 关联单号
     * */
    @ApiModelProperty(value="关联单号")
    private String referenceOrderNo;
    /**
     * 供应商
     * */
    @ApiModelProperty(value="供应商")
    private String supplier;
    /**
     * 供应商名称
     * */
    @ApiModelProperty(value="供应商名称")
    private String supplierName;

}
