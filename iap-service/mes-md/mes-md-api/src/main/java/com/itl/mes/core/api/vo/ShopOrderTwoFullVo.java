package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetSaveDto;
import com.itl.mes.core.api.dto.ShopOrderPackRuleSaveDto;
import com.itl.mes.core.api.entity.Customer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "ShopOrderTwoFullVo", description = "工单Two维护VO")
public class ShopOrderTwoFullVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单bo")
    private String bo;

    @ApiModelProperty(value = "排程BO")
    private String scheduleBo;

    @ApiModelProperty(value = "排程编号")
    private String scheduleCode;

    @ApiModelProperty(value = "工单号")
    @NotBlank
    private String shopOrder;

    @ApiModelProperty(value = "工单描述")
    private String orderDesc;

    @ApiModelProperty(value = "工单状态")
    @NotBlank
    private String state;

    /*@ApiModelProperty(value = "客户订单")
    private String customerOrder;*/

    /*@ApiModelProperty(value = "是否允许超产(Y/N)")
    private String isOverfulfill;

    @ApiModelProperty(value = "超产数量")
    private BigDecimal overfulfillQty;*/

    @ApiModelProperty(value = "排产数量")
    private Integer schedulQty;

    @Min(value = 1, message="订单数量必须大于0")
    @ApiModelProperty(value = "订单数量")
    private BigDecimal orderQty;

    @ApiModelProperty(value = "生产数量")
    private BigDecimal releaseQty;

    @ApiModelProperty(value = "标签数量")
    private BigDecimal labelQty;

    @ApiModelProperty(value = "生产物料Bo")
    private String itemBo;

    @ApiModelProperty(value = "生产物料")
    @NotBlank
    private String itemCode;

    @ApiModelProperty(value = "物料版本")
    @NotBlank
    private String itemVersion;

    @ApiModelProperty(value = "生产物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    /*@ApiModelProperty(value = "计划产线-编号")
    private String productLine;*/

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

    @ApiModelProperty(value ="班次名称")
    private String shiftName;

    @ApiModelProperty(value ="班次描述")
    private String shiftDesc;

    @ApiModelProperty(value = "计划工艺路线")
    private String router;

    @ApiModelProperty(value = "计划工艺路线版本")
    private String routerVersion;

    @ApiModelProperty(value = "工序BOM")
    private String bom;

    @ApiModelProperty(value = "计划物料清单版本")
    private String bomVersion;

    @ApiModelProperty(value = "计划结束日期")
    @TableField("PLAN_END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planEndDate;

    @ApiModelProperty(value = "计划开始日期")
    @TableField("PLAN_START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planStartDate;

    @ApiModelProperty(value = "修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @Valid
    @ApiModelProperty(value = "自定义数据属性和值")
    private List<CustomDataAndValVo> customDataAndValVoList;

    @Valid
    @ApiModelProperty(value = "自定义数据属性和值，保存时传入")
    private List<CustomDataValVo> customDataValVoList;

    @Valid
    @ApiModelProperty(value = "工单BOM集合")
    private ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto;

    @Valid
    @ApiModelProperty(value = "工序BOM集合")
    private ShopOrderBomComponnetSaveDto processBomComponnetSaveDto;

    @Valid
    @ApiModelProperty(value = "工单包装规则")
    private ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date negotiationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fixedTime;

    @ApiModelProperty(value = "紧急状态")
    private String emergencyState;

    @ApiModelProperty(value = "加急备注")
    private String emergencyBz;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "订单交期")
    private Date orderDeliveryTime;

    @ApiModelProperty(value = "工单类型")
    private String shopOrderType;

    @ApiModelProperty(value = "实际开工时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualStartTime;

    @ApiModelProperty(value = "实际完工时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualEndTime;

    @ApiModelProperty(value = "工单BOMBo")
    private String orderBomBo;

    @ApiModelProperty(value = "工单BOM")
    private String orderBom;

    @ApiModelProperty(value = "工单BOM版本")
    private String orderBomVersion;

    @ApiModelProperty(value = "恢复状态")
    private String recoveryStatus;

    @ApiModelProperty(value = "客户对象")
    private Customer customer;

    @ApiModelProperty(value = "erp生产订单Bo")
    private String erpOrderBo;

    @ApiModelProperty(value = "erp生产订单编号")
    private String erpOrderCode;

    @ApiModelProperty(value = "erp生产订单描述")
    private String erpOrderDesc;

    private String processChar;

    private String screwCombination;

    private String colourSys;

    private String site;
}
