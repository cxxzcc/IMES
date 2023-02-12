package com.itl.mes.core.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *   采集记录
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_collection_record")
@ApiModel(value="CollectionRecord对象", description="")
public class CollectionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "条码")
    private String barCode;

    @ApiModelProperty(value = "工单编号")
    private String workOrderNumber;

    @ApiModelProperty(value = "是否Hold 1是 0否 默认0")
    private Integer hold;

    @ApiModelProperty(value = "工单类型，取字典 ORDER_TYPE_STATE")
    private String workOrderType;

    @ApiModelProperty(value = "工单类型说明")
    @TableField(exist = false)
    private String workOrderTypeDesc;

    @ApiModelProperty(value = "工单数量")
    private Integer workCount;

    @ApiModelProperty(value = "工艺流程名称")
    private String processName;
    @ApiModelProperty(value = "工艺流程id")
    private String processId;

    @ApiModelProperty(value = "车间, WorkShop.workShop")
    private String workshop;

    @ApiModelProperty(value = "产线, ProductLine.productLine")
    private String productionLine;

    @ApiModelProperty(value = "产品编码")
    private String productCode;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "当前工序, Operation.operation")
    private String currentProcess;

    @ApiModelProperty(value = "下工序, Operation.operation")
    private String nextProcess;

    @ApiModelProperty(value = "是否已完工 1是 0否 默认0")
    private Integer complete;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "创建人用户名")
    private String createUser;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty(value = "更新人用户名")
    private String updateUser;

    /**
     * 班次名称
     * */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String shiftName;
    /**
     * 班次id
     * */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String shiftId;


}
