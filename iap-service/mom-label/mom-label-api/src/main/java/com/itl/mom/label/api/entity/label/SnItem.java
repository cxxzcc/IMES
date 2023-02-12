package com.itl.mom.label.api.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 物料标签管理
 * </p>
 *
 * @author space
 * @since 2019-10-25
 */
@TableName("z_sn_item")
@ApiModel("物料标签管理表")
@Data
@Accessors(chain = true)
public class SnItem {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "条码bo")
    private String snBo;

    @ApiModelProperty(value = "工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "物料bo")
    private String itemBo;

    @ApiModelProperty(value = "剩余数量默认批量数")
    private BigDecimal sysl;

    @ApiModelProperty(value = "批次")
    private String pc;

    @ApiModelProperty(value = "是否挂起 N否 Y是")
    private String sfgq;

    @ApiModelProperty(value = "站点")
    private String site;


}
