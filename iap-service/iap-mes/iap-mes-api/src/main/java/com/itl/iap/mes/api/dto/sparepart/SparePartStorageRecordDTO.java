package com.itl.iap.mes.api.dto.sparepart;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 备件出入库记录信息
 * @author dengou
 * @date 2021/9/17
 */
@Data
public class SparePartStorageRecordDTO implements Serializable {

    /**
     * 出入库单id
     * */
    @ApiModelProperty(value="出入库单id")
    private String id;
    /**
     * 详细记录id
     * */
    @ApiModelProperty(value="详细记录id")
    private String detailId;
    /**
     * 出库单号
     * */
    @ApiModelProperty(value="出库单号")
    private String orderNo;
    /**
     * 日期
     * */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value="出库日期")
    private Date recordTime;
    /**
     * 仓库
     * */
    @ApiModelProperty(value="仓库名称")
    private String wareHouseName;
    /**
     * 类型
     * */
    @ApiModelProperty(value="类型")
    private String type;
    /**
     * 类型说明
     * */
    @ApiModelProperty(value="类型说明")
    private String typeDesc;
    /**
     * 出库数量
     * */
    @ApiModelProperty(value="出库数量")
    private Integer outCount;
    /**
     * 入库数量
     * */
    @ApiModelProperty(value="入库数量")
    private Integer inCount;
    /**
     * 库存
     * */
    @ApiModelProperty(value="库存")
    private Integer inventory;
    /**
     * 单价
     * */
    @ApiModelProperty(value="单价")
    private BigDecimal price;
    /**
     * 金额
     * */
    @ApiModelProperty(value="金额")
    private BigDecimal totalAmount;
    /**
     * 经办人
     * */
    @ApiModelProperty(value="经办人")
    private String agent;
    /**
     * 关联单号
     * */
    @ApiModelProperty(value="关联单号")
    private String referenceOrderNo;
    /**
     * 部门id
     * */
    @ApiModelProperty(value="部门id，先不填")
    private String organizationId;

    /**
     * 备件名
     * */
    @ApiModelProperty(value="备件名称")
    private String sparePartName;
    /**
     * 备件编号
     * */
    @ApiModelProperty(value="备件编号")
    private String sparePartNo;
    /**
     * 备件类型code
     * */
    @ApiModelProperty(value="备件类型code")
    private String sparePartTypeCode;
    /**
     * 备件类型名称
     * */
    @ApiModelProperty(value="备件类型名称")
    private String sparePartTypeDesc;
    /**
     * 客户
     * */
    @ApiModelProperty(hidden = true)
    private String customer;
    /**
     * 客户名称
     * */
    @ApiModelProperty(value="客户名称")
    private String customerName;
    /**
     * 备注
     * */
    @ApiModelProperty(value="备注")
    private String remark;



}
