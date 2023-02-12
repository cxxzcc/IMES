package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetSaveDto;
import com.itl.mes.core.api.dto.ShopOrderPackRuleSaveDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel(value = "ShopOrderTwoSaveVo", description = "工单Two维护VO")
public class ShopOrderTwoSaveVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单bo")
    private String bo;

    @ApiModelProperty(value = "工单号")
    @NotBlank
    private String shopOrder;

    @ApiModelProperty(value = "工单描述")
    private String orderDesc;

    @ApiModelProperty(value = "工单类型")
    private String shopOrderType;

    @ApiModelProperty(value = "工单状态")
    @NotBlank
    private String state;

    @ApiModelProperty(value = "工单数量")
    private BigDecimal schedulQty;

    @ApiModelProperty(value = "工单标签数量")
    private BigDecimal labelQty;

    @ApiModelProperty(value = "生产物料Bo")
    private String itemBo;

    @ApiModelProperty(value = "生产物料")
    @NotBlank
    private String itemCode;

    @ApiModelProperty(value = "物料版本")
    @NotBlank
    private String itemVersion;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value ="产线BO")
    private String productionLineBo;

    @ApiModelProperty(value ="产线编号")
    private String productionLineCode;

    @ApiModelProperty(value ="产线描述")
    private String productionLineDesc;

    @ApiModelProperty(value ="班次BO")
    private String shiftBo;

    @ApiModelProperty(value ="班次编号")
    private String shiftCode;

    @ApiModelProperty(value = "计划开始日期")
    @TableField("PLAN_START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planStartDate;

    @ApiModelProperty(value = "计划结束日期")
    @TableField("PLAN_END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planEndDate;

    @ApiModelProperty(value = "修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "计划工艺路线")
    private String router;

    @ApiModelProperty(value = "计划工艺路线版本")
    private String routerVersion;

    @ApiModelProperty(value = "计划物料清单版本")
    private String bomVersion;


    @ApiModelProperty(value = "工单BOMBo")
    private String orderBomBo;

    @ApiModelProperty(value = "工单BOM")
    private String orderBom;

    @ApiModelProperty(value = "工单BOM版本")
    private String orderBomVersion;

    @Valid
    @ApiModelProperty(value = "工单BOM集合")
    private ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto;

    @ApiModelProperty(value = "工序bomBo")
    private String bomBo;

    @ApiModelProperty(value = "工序BOM")
    private String bom;

    @Valid
    @ApiModelProperty(value = "工序BOM集合")
    private ShopOrderBomComponnetSaveDto processBomComponnetSaveDto;

    @Valid
    @ApiModelProperty(value = "工单包装规则")
    private ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto;

    @ApiModelProperty(value = "工厂编码")
    private String site;

    @ApiModelProperty(value = "erp订单Bo")
    private String erpOrderBo;

    @ApiModelProperty(value = "erp订单编号")
    private String erpOrderCode;

    @ApiModelProperty(value = "erp订单描述")
    private String erpOrderDesc;

    @ApiModelProperty(value = "客户Bo")
    private String customerBo;

    @ApiModelProperty(value = "客户编号")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

}
