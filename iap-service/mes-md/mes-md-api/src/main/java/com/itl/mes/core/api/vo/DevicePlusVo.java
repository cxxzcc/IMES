package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@ApiModel(value="Device",description="设备")
@FieldNameConstants
@Data
public class DevicePlusVo {

   @ApiModelProperty(value="RES:SITE,DEVICE【PK】")
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   private String site;

   @ApiModelProperty(value="设备编号【UK】")
   private String device;

   @ApiModelProperty(value="设备名称")
   private String deviceName;

   @ApiModelProperty(value="设备类型")
   private String deviceType;

   @ApiModelProperty(value="设备描述")
   private String deviceDesc;

   @ApiModelProperty(value="设备型号")
   private String deviceModel;

   @ApiModelProperty(value="状态")
   private String state;

   @ApiModelProperty(value="是否为加工设备")
   private String isProcessDevice;

   @ApiModelProperty(value="产线【M_PRODUCT_LINE的BO】")
   private String productLineBo;

   @ApiModelProperty(value="工位【M_STATION的BO】")
   private String stationBo;

   @ApiModelProperty(value="安装地点")
   private String location;

   @ApiModelProperty(value="资产编号")
   private String assetsCode;

   @ApiModelProperty(value="生产厂家")
   private String manufacturer;

   @ApiModelProperty(value="投产日期")
   private Date validStartDate;

   @ApiModelProperty(value="结束日期")
   private Date validEndDate;

   @ApiModelProperty(value="备注")
   private String memo;

   @ApiModelProperty(value="建档日期")
   private Date createDate;

   @ApiModelProperty(value="建档人")
   private String createUser;

   @ApiModelProperty(value="修改人")
   private String modifyUser;

   @ApiModelProperty(value="修改日期")
   private Date modifyDate;

   @ApiModelProperty(value="车间")
   private String workShop;

   @ApiModelProperty(value="螺杆组合")
   private String screwCombination;

   @ApiModelProperty(value = "负责人")
   private String incharge;

   @ApiModelProperty(value = "联系方式")
   private String tel;

   @ApiModelProperty( value = "使用寿命" )
   private String usefulLife;

   @ApiModelProperty( value = "封面图" )
   private String coverImg;

   @ApiModelProperty( value = "购买日期" )
   private Date buyDate;

   @ApiModelProperty( value = "供应商id" )
   private String supplier;


}
