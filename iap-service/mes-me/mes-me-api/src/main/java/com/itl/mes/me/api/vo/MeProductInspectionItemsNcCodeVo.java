package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 产品检验项不合格代码表
 * </p>
 *
 * @author chenjx1
 * @since 2021-12-08
 */
@Data
@ApiModel(value="MeProductInspectionItemsNcCodeVo",description="产品检验项不合格代码表")
public class MeProductInspectionItemsNcCodeVo implements Serializable {

   @ApiModelProperty(value="UUID")
   private String bo;

   @ApiModelProperty(value="产品检验项目ID")
   private Integer inspectionItemId;

   /**
    * 产品检验项目类型（0产品、1产品组）
    */
   @ApiModelProperty("产品检验项目类型（0产品、1产品组）")
   private String itemType;

   @ApiModelProperty(value="产品检验项目不良代码序号（排序）")
   private Integer serialNum;

   @ApiModelProperty(value="不良BO")
   private String ncBo;

   @ApiModelProperty(value="不良代码编号")
   private String ncCode;

   @ApiModelProperty(value="不良代码名称")
   private String ncName;

   @ApiModelProperty(value="不良代码描述")
   private String ncDesc;

   @ApiModelProperty(value="不良类型[F故障、D缺陷、R修复]")
   private String ncType;

   @ApiModelProperty(value="状态")
   private String state;

   @ApiModelProperty(value="优先级")
   private Integer priority;

   @ApiModelProperty(value="最大不良限制(SFC)")
   private Integer maxNcLimit;

   @ApiModelProperty(value="严重性[0无、2低、3中、5高，缺省为3中]")
   private String severity;

   @ApiModelProperty(value="工厂编号")
   private String site;
}
