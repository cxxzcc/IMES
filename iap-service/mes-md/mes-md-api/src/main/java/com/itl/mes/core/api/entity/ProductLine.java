package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产线表
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
@TableName("m_product_line")
@ApiModel(value="ProductLine",description="产线表")
@Data
public class ProductLine extends Model<ProductLine> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="WC:SITE,PRODUCT_LINE【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   @Excel(name = "产线BO")
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   @Excel(name = "工厂")
   private String site;

   @ApiModelProperty(value="产线【UK】")
   @Length( max = 64 )
   @TableField("PRODUCT_LINE")
   @Excel(name = "产线编码")
   private String productLine;

   @ApiModelProperty(value="产线描述")
   @Length( max = 200 )
   @TableField("PRODUCT_LINE_DESC")
   @Excel(name = "产线名称")
   private String productLineDesc;

   @ApiModelProperty(value="所属车间 BO【UK】")
   @Length( max = 100 )
   @TableField("WORK_SHOP_BO")
   @Excel(name = "所属车间BO")
   private String workShopBo;

   @ApiModelProperty(value="状态（已启用1，已禁用0）")
   @Length( max = 1 )
   @TableField("STATE")
   @Excel(name = "状态")
   private String state;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   @Excel(name = "建档日期")
   private Date createDate;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   @Excel(name = "建档人")
   private String createUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   @Excel(name = "修改日期")
   private Date modifyDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   @Excel(name = "修改人")
   private String modifyUser;

   @Length( max = 38 )
   @TableField("CALENDAR_SCHEME")
   private String calendarScheme;
   public static final String CALENDAR_SCHEME = "CALENDAR_SCHEME";
   @Length( max = 10 )
   @TableField("PERCENTAGE")
   private String percentage;
   public static final String PERCENTAGE = "PERCENTAGE";

   @TableField("RATED_TIME")
   private Integer ratedTime;
   public static final String RATED_TIME = "RATED_TIME";
   @TableField("MAX_TIME")
   private Integer maxTime;
   public static final String MAX_TIME = "MAX_TIME";
   @TableField("MAINTENANCE_TIME")
   private Integer maintenanceTime;
   public static final String MAINTENANCE_TIME = "MAINTENANCE_TIME";


   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String PRODUCT_LINE = "PRODUCT_LINE";

   public static final String PRODUCT_LINE_DESC = "PRODUCT_LINE_DESC";

   public static final String WORK_SHOP_BO = "WORK_SHOP_BO";

   public static final String STATE = "STATE";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";





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
      return "ProductLine{" +
         ", bo = " + bo +
         ", site = " + site +
         ", productLine = " + productLine +
         ", productLineDesc = " + productLineDesc +
         ", workShopBo = " + workShopBo +
         ", state = " + state +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }

}