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
 * 配置项
 * </p>
 *
 * @author chenjx1
 * @since 2021-11-9
 */
@TableName("m_config_item")
@ApiModel(value="ConfigItem",description="配置项表")
public class ConfigItem extends Model<ConfigItem> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="UUID【PK】")
   @Length( max = 200 )
   @TableId(value = "BO", type = IdType.UUID)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="编号【UK】")
   @Length( max = 64 )
   @TableField("ITEM_CODE")
   private String itemCode;

   @ApiModelProperty(value="名称")
   @Length( max = 64 )
   @TableField("ITEM_NAME")
   private String itemName;

   @ApiModelProperty(value="描述")
   @Length( max = 200 )
   @TableField("ITEM_DESC")
   private String itemDesc;

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

   @ApiModelProperty(value="状态（0：未启用，1：已启用）")
   @Length( max = 1 )
   @TableField("STATE")
   private Integer state;

   @ApiModelProperty(value="配置项KEY")
   @Length( max = 32 )
   @TableField("CONFIG_ITEM_KEY")
   private String configItemKey;

   @ApiModelProperty(value="配置项VALUE")
   @Length( max = 32 )
   @TableField("CONFIG_ITEM_VALUE")
   private String configItemValue;

   @ApiModelProperty(value="配置项TYPE")
   @Length( max = 32 )
   @TableField("CONFIG_ITEM_TYPE")
   private String configItemType;


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

   public String getItemCode() {
      return itemCode;
   }

   public void setItemCode(String itemCode) {
      this.itemCode = itemCode;
   }

   public String getItemName() {
      return itemName;
   }

   public void setItemName(String itemName) {
      this.itemName = itemName;
   }

   public String getItemDesc() {
      return itemDesc;
   }

   public void setItemDesc(String itemDesc) {
      this.itemDesc = itemDesc;
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

   public Integer getState() {
      return state;
   }

   public void setState(Integer state) {
      this.state = state;
   }

   public String getConfigItemKey() {
      return configItemKey;
   }

   public void setConfigItemKey(String configItemKey) {
      this.configItemKey = configItemKey;
   }

   public String getConfigItemValue() {
      return configItemValue;
   }

   public void setConfigItemValue(String configItemValue) {
      this.configItemValue = configItemValue;
   }

   public String getConfigItemType() {
      return configItemType;
   }

   public void setConfigItemType(String configItemType) {
      this.configItemType = configItemType;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String ITEM_CODE = "ITEM_CODE";

   public static final String ITEM_NAME = "ITEM_NAME";

   public static final String ITEM_DESC = "ITEM_DESC";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String STATE = "STATE";

   public static final String CONFIG_ITEM_KEY = "CONFIG_ITEM_KEY";

   public static final String CONFIG_ITEM_VALUE = "CONFIG_ITEM_VALUE";

   public static final String CONFIG_ITEM_TYPE = "CONFIG_ITEM_TYPE";

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
      return "ConfigItem{" +
              "bo='" + bo + '\'' +
              ", site='" + site + '\'' +
              ", itemCode='" + itemCode + '\'' +
              ", itemName='" + itemName + '\'' +
              ", itemDesc='" + itemDesc + '\'' +
              ", createDate=" + createDate +
              ", createUser='" + createUser + '\'' +
              ", modifyDate=" + modifyDate +
              ", modifyUser='" + modifyUser + '\'' +
              ", state=" + state +
              ", configItemKey='" + configItemKey + '\'' +
              ", configItemValue='" + configItemValue + '\'' +
              ", configItemType='" + configItemType + '\'' +
              '}';
   }
}
