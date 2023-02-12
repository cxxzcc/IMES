package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 装配临时表
 *
 * @author cch
 * @date 2021-06-08
 */
@Data
@TableName("me_assy_temp")
@ApiModel(value = "me_assy_temp", description = "装配临时表")
@Accessors(chain = true)
public class AssyTemp implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private String id;

    @TableField("SITE")
    @ApiModelProperty(value = "工厂")
    private String site;

    @TableField("SFC")
    @ApiModelProperty(value = "SFC编号【FK】")
    private String sfc;

    @TableField("TRACE_METHOD")
    @ApiModelProperty(value = "追溯方式")
    private String traceMethod;

    @TableField("COMPONENET_BO")
    @ApiModelProperty(value = "装配组件")
    private String componenetBo;

    @TableField("ASSEMBLED_SN")
    @ApiModelProperty(value = "装配SN")
    private String assembledSn;

    @TableField("QTY")
    @ApiModelProperty(value = "装配数量")
    private BigDecimal qty;

    @TableField("ASSY_TIME")
    @ApiModelProperty(value = "装配时间")
    private Date assyTime;

    @TableField("ASSY_USER")
    @ApiModelProperty(value = "装配用户")
    private String assyUser;

    @TableField("COMPONENT_STATE")
    @ApiModelProperty(value = "组件SN状态")
    private Integer componentState;

}
