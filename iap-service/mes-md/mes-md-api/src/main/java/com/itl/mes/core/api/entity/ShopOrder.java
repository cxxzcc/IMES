package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工单表
 * </p>
 *
 * @author space
 * @since 2019-06-18
 */
@TableName("m_shop_order")
@ApiModel(value="ShopOrder",description="工单表")
@Data
public class ShopOrder extends Model<ShopOrder> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="SO:SITE,SHOP_ORDER【PK】")
    @Length( max = 100 )
    @NotBlank
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="工单号【UK】")
    @Length( max = 64 )
    @NotBlank
    @TableField("SHOP_ORDER")
    private String shopOrder;

    @ApiModelProperty(value="客户订单")
    @Length( max = 100 )
    @TableField("CUSTOMER_ORDER_BO")
    private String customerOrderBo;

    @ApiModelProperty(value="工单描述")
    @Length( max = 50 )
    @TableField("ORDER_DESC")
    private String orderDesc;

    @ApiModelProperty(value="工单状态")
    @Length( max = 100 )
    @NotBlank
    @TableField("STATE_BO")
    private String stateBo;

    @ApiModelProperty(value="是否允许超产(Y/N)")
    @Length( max = 1 )
    /*@NotBlank
    @Pattern( regexp = "^(Y|N)$" , message = "值必须为Y或者N" )*/
    @TableField("IS_OVERFULFILL")
    private String isOverfulfill;

    @ApiModelProperty(value="超产数量")
    @TableField("OVERFULFILL_QTY")
    private BigDecimal overfulfillQty;

    @ApiModelProperty(value="排产数量")
    @TableField("SCHEDUL_QTY")
    private BigDecimal schedulQty;

    @ApiModelProperty(value="完成数量")
    @TableField("COMPLETE_QTY")
    private BigDecimal completeQty;

    @ApiModelProperty(value="订单数量")
    @TableField("ORDER_QTY")
    private BigDecimal orderQty;

    @ApiModelProperty(value="下达数量(SFC生成数量)")
    @TableField("RELEASE_QTY")
    private BigDecimal releaseQty;

    @ApiModelProperty(value="SFC报废数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="打印标签数量")
    @TableField("LABEL_QTY")
    private BigDecimal labelQty;

    @ApiModelProperty(value="生产物料")
    @Length( max = 100 )
    @NotBlank
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="计划产线")
    @Length( max = 100 )
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value="班次BO")
    @Length( max = 100 )
    @TableField("SHIFT_BO")
    private String shiftBo;

    @ApiModelProperty(value="计划工艺路线")
    @Length( max = 100 )
    @TableField("ROUTER_BO")
    private String routerBo;

    @ApiModelProperty(value="工序BOM")
    @Length( max = 100 )
    @TableField("BOM_BO")
    private String bomBo;

    @ApiModelProperty(value="计划结束日期")
    @TableField("PLAN_END_DATE")
    private Date planEndDate;

    @ApiModelProperty(value="计划开始日期")
    @TableField("PLAN_START_DATE")
    private Date planStartDate;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @ApiModelProperty(value="协商交期")
    @TableField("NEGOTIATION_TIME")
    private Date negotiationTime;

    @ApiModelProperty(value="固定交期")
    @TableField("FIXED_TIME")
    private Date fixedTime;

    @ApiModelProperty(value="紧急状态")
    @TableField("EMERGENCY_STATE")
    private String emergencyState;

    @ApiModelProperty(value="加急备注")
    @TableField("EMERGENCY_BZ")
    private String emergencyBz;

    @ApiModelProperty(value="订单交期")
    @TableField("ORDER_DELIVERY_TIME")
    private Date orderDeliveryTime;

    @ApiModelProperty(value="工单类型")
    @TableField("SHOP_ORDER_TYPE")
    private String shopOrderType;

    @ApiModelProperty(value="实际开工时间")
    @TableField("ACTUAL_START_TIME")
    private Date actualStartTime;

    @ApiModelProperty(value="实际完工时间")
    @TableField("ACTUAL_END_TIME")
    private Date actualEndTime;

    @ApiModelProperty(value="工单BOM")
    @TableField("ORDER_BOM_BO")
    private String orderBomBo;

    @ApiModelProperty(value="恢复状态")
    @TableField("RECOVERY_STATUS")
    private String recoveryStatus;

    @TableField(exist = false)
    private String itemDesc;

    @TableField(exist = false)
    private String processChar;

    @TableField(exist = false)
    private String colourSys;

    @TableField(exist = false)
    private String screwCombination;

    @ApiModelProperty(value = "erp生产订单Bo")
    @TableField("ERP_ORDER_BO")
    private String erpOrderBo;

    @ApiModelProperty(value = "erp生产订单编号")
    @TableField("ERP_ORDER_CODE")
    private String erpOrderCode;

    @ApiModelProperty(value = "erp生产订单描述")
    @TableField("ERP_ORDER_DESC")
    private String erpOrderDesc;

    @ApiModelProperty(value = "客户Bo")
    @TableField("CUSTOMER_BO")
    private String customerBo;


    public ShopOrder() {
    }

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getShopOrder() {
        return shopOrder;
    }

    public void setShopOrder(String shopOrder) {
        this.shopOrder = shopOrder;
    }

    public String getCustomerOrderBo() {
        return customerOrderBo;
    }

    public void setCustomerOrderBo(String customerOrderBo) {
        this.customerOrderBo = customerOrderBo;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getStateBo() {
        return stateBo;
    }

    public void setStateBo(String stateBo) {
        this.stateBo = stateBo;
    }

    public String getIsOverfulfill() {
        return isOverfulfill;
    }

    public void setIsOverfulfill(String isOverfulfill) {
        this.isOverfulfill = isOverfulfill;
    }

    public BigDecimal getOverfulfillQty() {
        return overfulfillQty;
    }

    public void setOverfulfillQty(BigDecimal overfulfillQty) {
        this.overfulfillQty = overfulfillQty;
    }

    public BigDecimal getSchedulQty() {
        return schedulQty;
    }

    public void setSchedulQty(BigDecimal schedulQty) {
        this.schedulQty = schedulQty;
    }

    public BigDecimal getCompleteQty() {
        return completeQty;
    }

    public void setCompleteQty(BigDecimal completeQty) {
        this.completeQty = completeQty;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getReleaseQty() {
        return releaseQty;
    }

    public void setReleaseQty(BigDecimal releaseQty) {
        this.releaseQty = releaseQty;
    }

    public BigDecimal getScrapQty() {
        return scrapQty;
    }

    public void setScrapQty(BigDecimal scrapQty) {
        this.scrapQty = scrapQty;
    }

    public BigDecimal getLabelQty() {
        return labelQty;
    }

    public void setLabelQty(BigDecimal labelQty) {
        this.labelQty = labelQty;
    }

    public String getItemBo() {
        return itemBo;
    }

    public void setItemBo(String itemBo) {
        this.itemBo = itemBo;
    }

    public String getProductLineBo() {
        return productLineBo;
    }

    public void setProductLineBo(String productLineBo) {
        this.productLineBo = productLineBo;
    }

    public String getShiftBo() {
        return shiftBo;
    }

    public void setShiftBo(String shiftBo) {
        this.shiftBo = shiftBo;
    }

    public String getRouterBo() {
        return routerBo;
    }

    public void setRouterBo(String routerBo) {
        this.routerBo = routerBo;
    }

    public String getBomBo() {
        return bomBo;
    }

    public void setBomBo(String bomBo) {
        this.bomBo = bomBo;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getNegotiationTime() {
        return negotiationTime;
    }

    public void setNegotiationTime(Date negotiationTime) {
        this.negotiationTime = negotiationTime;
    }

    public Date getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(Date fixedTime) {
        this.fixedTime = fixedTime;
    }

    public String getEmergencyState() {
        return emergencyState;
    }

    public void setEmergencyState(String emergencyState) {
        this.emergencyState = emergencyState;
    }

    public String getEmergencyBz() {
        return emergencyBz;
    }

    public void setEmergencyBz(String emergencyBz) {
        this.emergencyBz = emergencyBz;
    }

    public Date getOrderDeliveryTime() {
        return orderDeliveryTime;
    }

    public void setOrderDeliveryTime(Date orderDeliveryTime) {
        this.orderDeliveryTime = orderDeliveryTime;
    }

    public String getShopOrderType() {
        return shopOrderType;
    }

    public void setShopOrderType(String shopOrderType) {
        this.shopOrderType = shopOrderType;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getOrderBomBo() {
        return orderBomBo;
    }

    public void setOrderBomBo(String orderBomBo) {
        this.orderBomBo = orderBomBo;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    public void setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getProcessChar() {
        return processChar;
    }

    public void setProcessChar(String processChar) {
        this.processChar = processChar;
    }

    public String getColourSys() {
        return colourSys;
    }

    public void setColourSys(String colourSys) {
        this.colourSys = colourSys;
    }

    public String getScrewCombination() {
        return screwCombination;
    }

    public void setScrewCombination(String screwCombination) {
        this.screwCombination = screwCombination;
    }

    public String getErpOrderBo() {
        return erpOrderBo;
    }

    public void setErpOrderBo(String erpOrderBo) {
        this.erpOrderBo = erpOrderBo;
    }

    public String getErpOrderCode() {
        return erpOrderCode;
    }

    public void setErpOrderCode(String erpOrderCode) {
        this.erpOrderCode = erpOrderCode;
    }

    public String getErpOrderDesc() {
        return erpOrderDesc;
    }

    public void setErpOrderDesc(String erpOrderDesc) {
        this.erpOrderDesc = erpOrderDesc;
    }

    public String getCustomerBo() {
        return customerBo;
    }

    public void setCustomerBo(String customerBo) {
        this.customerBo = customerBo;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String SHOP_ORDER = "SHOP_ORDER";

    public static final String CUSTOMER_ORDER_BO = "CUSTOMER_ORDER_BO";

    public static final String ORDER_DESC = "ORDER_DESC";

    public static final String STATE_BO = "STATE_BO";

    public static final String IS_OVERFULFILL = "IS_OVERFULFILL";

    public static final String OVERFULFILL_QTY = "OVERFULFILL_QTY";

    public static final String SCHEDUL_QTY = "SCHEDUL_QTY";

    public static final String COMPLETE_QTY = "COMPLETE_QTY";

    public static final String ORDER_QTY = "ORDER_QTY";

    public static final String RELEASE_QTY = "RELEASE_QTY";

    public static final String SCRAP_QTY = "SCRAP_QTY";

    public static final String ITEM_BO = "ITEM_BO";

    public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

    public static final String SHIFT_BO = "SHIFT_BO";

    public static final String ROUTER_BO = "ROUTER_BO";

    public static final String BOM_BO = "BOM_BO";

    public static final String PLAN_END_DATE = "PLAN_END_DATE";

    public static final String PLAN_START_DATE = "PLAN_START_DATE";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String MODIFY_USER = "MODIFY_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    public static final String ERP_ORDER_BO = "ERP_ORDER_BO";

    public static final String CUSTOMER_BO = "CUSTOMER_BO";


    @Override
    protected Serializable pkVal() {
        return this.bo;
    }


    public void setObjectSetBasicAttribute( String userId,Date date ){
        this.createUser=userId;
        this.createDate=date;
        this.modifyUser=userId;
        this.modifyDate=date;
    }

    @Override
    public String toString() {
        return "ShopOrder{" +
                "bo='" + bo + '\'' +
                ", site='" + site + '\'' +
                ", shopOrder='" + shopOrder + '\'' +
                ", customerOrderBo='" + customerOrderBo + '\'' +
                ", orderDesc='" + orderDesc + '\'' +
                ", stateBo='" + stateBo + '\'' +
                ", isOverfulfill='" + isOverfulfill + '\'' +
                ", overfulfillQty=" + overfulfillQty +
                ", schedulQty=" + schedulQty +
                ", completeQty=" + completeQty +
                ", orderQty=" + orderQty +
                ", releaseQty=" + releaseQty +
                ", scrapQty=" + scrapQty +
                ", labelQty=" + labelQty +
                ", itemBo='" + itemBo + '\'' +
                ", productLineBo='" + productLineBo + '\'' +
                ", shiftBo='" + shiftBo + '\'' +
                ", routerBo='" + routerBo + '\'' +
                ", bomBo='" + bomBo + '\'' +
                ", planEndDate=" + planEndDate +
                ", planStartDate=" + planStartDate +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", modifyUser='" + modifyUser + '\'' +
                ", modifyDate=" + modifyDate +
                ", negotiationTime=" + negotiationTime +
                ", fixedTime=" + fixedTime +
                ", emergencyState='" + emergencyState + '\'' +
                ", emergencyBz='" + emergencyBz + '\'' +
                ", orderDeliveryTime=" + orderDeliveryTime +
                ", shopOrderType='" + shopOrderType + '\'' +
                ", actualStartTime=" + actualStartTime +
                ", actualEndTime=" + actualEndTime +
                ", orderBomBo='" + orderBomBo + '\'' +
                ", recoveryStatus='" + recoveryStatus + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", processChar='" + processChar + '\'' +
                ", colourSys='" + colourSys + '\'' +
                ", screwCombination='" + screwCombination + '\'' +
                ", erpOrderBo='" + erpOrderBo + '\'' +
                ", erpOrderCode='" + erpOrderCode + '\'' +
                ", erpOrderDesc='" + erpOrderDesc + '\'' +
                ", customerBo='" + customerBo + '\'' +
                '}';
    }
}
