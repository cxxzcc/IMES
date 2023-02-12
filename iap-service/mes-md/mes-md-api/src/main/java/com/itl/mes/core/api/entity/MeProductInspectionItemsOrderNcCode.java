package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品检验项不合格代码表-工单副本
 * </p>
 *
 * @author chenjx1
 * @since 2021-12-10
 */
@TableName("me_product_inspection_items_order_nc_code")
@ApiModel(value="MeProductInspectionItemsOrderNcCode",description="产品检验项不合格代码表-工单副本")
public class MeProductInspectionItemsOrderNcCode extends Model<MeProductInspectionItemsOrderNcCode> {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="UUID")
   @Length( max = 200 )
   @TableId(type = IdType.UUID)
   private String bo;

   @ApiModelProperty(value="产品检验项目ID")
   @TableField("INSPECTION_ITEM_ID")
   private Integer inspectionItemId;

   /**
    * 产品检验项目类型（0产品、1产品组）
    */
   @TableField("ITEM_TYPE")
   @ApiModelProperty("产品检验项目类型（0产品、1产品组）")
   private String itemType;

   @ApiModelProperty(value="工厂编号")
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="关联工单")
   @TableField("ORDER_BO")
   private String orderBo;

   @ApiModelProperty(value="产品检验项目不良代码序号（排序）")
   @TableField("SERIAL_NUM")
   private Integer serialNum;

   @ApiModelProperty(value="不良BO")
   @Length( max = 200 )
   @TableField("NC_BO")
   private String ncBo;

   @ApiModelProperty(value="不良代码编号")
   @Length( max = 200 )
   @TableField("NC_CODE")
   private String ncCode;

   @ApiModelProperty(value="不良代码名称")
   @Length( max = 64 )
   @TableField("NC_NAME")
   private String ncName;

   @ApiModelProperty(value="不良代码描述")
   @Length( max = 200 )
   @TableField("NC_DESC")
   private String ncDesc;

   @ApiModelProperty(value="不良类型[F故障、D缺陷、R修复]")
   @Length( max = 32 )
   @TableField("NC_TYPE")
   private String ncType;

   @ApiModelProperty(value="状态")
   @Length( max = 32 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="优先级")
   @TableField("PRIORITY")
   private Integer priority;

   @ApiModelProperty(value="最大不良限制(SFC)")
   @TableField("MAX_NC_LIMIT")
   private Integer maxNcLimit;

   @ApiModelProperty(value="严重性[0无、2低、3中、5高，缺省为3中]")
   @Length( max = 1 )
   @TableField("SEVERITY")
   private String severity;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   private Date modifyDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;


   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
   }

   public Integer getInspectionItemId() {
      return inspectionItemId;
   }

   public void setInspectionItemId(Integer inspectionItemId) {
      this.inspectionItemId = inspectionItemId;
   }

   public String getItemType() {
      return itemType;
   }

   public void setItemType(String itemType) {
      this.itemType = itemType;
   }

   public String getSite() {
      return site;
   }

   public void setSite(String site) {
      this.site = site;
   }

   public String getOrderBo() {
      return orderBo;
   }

   public void setOrderBo(String orderBo) {
      this.orderBo = orderBo;
   }

   public Integer getSerialNum() {
      return serialNum;
   }

   public void setSerialNum(Integer serialNum) {
      this.serialNum = serialNum;
   }

   public String getNcBo() {
      return ncBo;
   }

   public void setNcBo(String ncBo) {
      this.ncBo = ncBo;
   }

   public String getNcCode() {
      return ncCode;
   }

   public void setNcCode(String ncCode) {
      this.ncCode = ncCode;
   }

   public String getNcName() {
      return ncName;
   }

   public void setNcName(String ncName) {
      this.ncName = ncName;
   }

   public String getNcDesc() {
      return ncDesc;
   }

   public void setNcDesc(String ncDesc) {
      this.ncDesc = ncDesc;
   }

   public String getNcType() {
      return ncType;
   }

   public void setNcType(String ncType) {
      this.ncType = ncType;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public Integer getPriority() {
      return priority;
   }

   public void setPriority(Integer priority) {
      this.priority = priority;
   }

   public Integer getMaxNcLimit() {
      return maxNcLimit;
   }

   public void setMaxNcLimit(Integer maxNcLimit) {
      this.maxNcLimit = maxNcLimit;
   }

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
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

   public Date getModifyDate() {
      return modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public String getModifyUser() {
      return modifyUser;
   }

   public void setModifyUser(String modifyUser) {
      this.modifyUser = modifyUser;
   }

   public static final String BO = "BO";

   public static final String INSPECTION_ITEM_ID = "INSPECTION_ITEM_ID";

   public static final String ITEM_TYPE = "ITEM_TYPE";

   public static final String SITE = "SITE";

   public static final String ORDER_BO = "ORDER_BO";

   public static final String SERIAL_NUM = "SERIAL_NUM";

   public static final String NC_BO = "NC_BO";

   public static final String NC_CODE = "NC_CODE";

   public static final String NC_NAME = "NC_NAME";

   public static final String NC_DESC = "NC_DESC";

   public static final String NC_TYPE = "NC_TYPE";

   public static final String STATE = "STATE";

   public static final String PRIORITY = "PRIORITY";

   public static final String MAX_NC_LIMIT = "MAX_NC_LIMIT";

   public static final String SEVERITY = "SEVERITY";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";


   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   @Override
   public String toString() {
      return "MeProductInspectionItemsOrderNcCode{" +
              "bo='" + bo + '\'' +
              ", inspectionItemId=" + inspectionItemId +
              ", itemType='" + itemType + '\'' +
              ", site='" + site + '\'' +
              ", orderBo='" + orderBo + '\'' +
              ", serialNum=" + serialNum +
              ", ncBo='" + ncBo + '\'' +
              ", ncCode='" + ncCode + '\'' +
              ", ncName='" + ncName + '\'' +
              ", ncDesc='" + ncDesc + '\'' +
              ", ncType='" + ncType + '\'' +
              ", state='" + state + '\'' +
              ", priority=" + priority +
              ", maxNcLimit=" + maxNcLimit +
              ", severity='" + severity + '\'' +
              ", createDate=" + createDate +
              ", createUser='" + createUser + '\'' +
              ", modifyDate=" + modifyDate +
              ", modifyUser='" + modifyUser + '\'' +
              '}';
   }

}
