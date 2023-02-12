package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 产品检验项不合格代码表
 * </p>
 *
 * @author chenjx1
 * @since 2021-12-08
 */
@TableName("me_product_inspection_items_nc_code")
@ApiModel(value="MeProductInspectionItemsNcCode",description="产品检验项不合格代码表")
public class MeProductInspectionItemsNcCode extends Model<MeProductInspectionItemsNcCode> {

   private static final long serialVersionUID = 1L;


   //@ApiModelProperty(value="NC:SITE,NC_BO【PK】")
   //@TableId(value = "BO", type = IdType.INPUT)
   @ApiModelProperty(value="UUID")
   @Length( max = 200 )
   @TableId(type = IdType.UUID)
   private String bo;

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

   @ApiModelProperty(value="产品检验项目不良代码序号（排序）")
   @TableField("SERIAL_NUM")
   private Integer serialNum;

   @ApiModelProperty(value="产品检验项目ID")
   @TableField("INSPECTION_ITEM_ID")
   private Integer inspectionItemId;

   /**
    * 产品检验项目类型（0产品、1产品组）
    */
   @TableField("ITEM_TYPE")
   @ApiModelProperty("产品检验项目类型（0产品、1产品组）")
   private String itemType;

   /**
    * 工厂编号
    */
   @TableField("SITE")
   @ApiModelProperty("工厂编号")
   private String site;



   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
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

   public Integer getSerialNum() {
      return serialNum;
   }

   public void setSerialNum(Integer serialNum) {
      this.serialNum = serialNum;
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

   public static final String BO = "BO";

   public static final String NC_BO = "NC_BO";

   public static final String NC_CODE = "NC_CODE";

   public static final String NC_NAME = "NC_NAME";

   public static final String NC_DESC = "NC_DESC";

   public static final String NC_TYPE = "NC_TYPE";

   public static final String SERIAL_NUM = "SERIAL_NUM";

   public static final String INSPECTION_ITEM_ID = "INSPECTION_ITEM_ID";

   public static final String ITEM_TYPE = "ITEM_TYPE";

   public static final String SITE = "SITE";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }

   @Override
   public String toString() {
      return "MeProductInspectionItemsNcCode{" +
              "bo='" + bo + '\'' +
              ", ncBo='" + ncBo + '\'' +
              ", ncCode='" + ncCode + '\'' +
              ", ncName='" + ncName + '\'' +
              ", ncDesc='" + ncDesc + '\'' +
              ", ncType='" + ncType + '\'' +
              ", serialNum=" + serialNum +
              ", inspectionItemId=" + inspectionItemId +
              ", itemType='" + itemType + '\'' +
              ", site='" + site + '\'' +
              '}';
   }
}
