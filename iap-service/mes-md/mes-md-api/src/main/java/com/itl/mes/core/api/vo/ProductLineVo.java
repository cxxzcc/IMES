package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="ProductLineVo",description="产线保存")
public class ProductLineVo implements Serializable {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="WC:SITE,PRODUCT_LINE【PK】")
   private String bo;

   @ApiModelProperty(value="产线")
   @Excel(name = "产线编码")
   private String productLine;

   @ApiModelProperty(value="产线描述")
   @Excel(name = "产线名称")
   private String productLineDesc;

   @ApiModelProperty(value="所属车间")
   @Excel(name = "所属车间")
   private String workShop;

   @ApiModelProperty(value="状态（已启用1，已禁用0）")
   @Excel(name = "状态")
   private String state;

   @ApiModelProperty( value = "自定义数据属性和值" )
   private List<CustomDataAndValVo> customDataAndValVoList;

   @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
   private List<CustomDataValVo> customDataValVoList;

   @ApiModelProperty(value="修改日期")
   @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
   private Date modifyDate;

}