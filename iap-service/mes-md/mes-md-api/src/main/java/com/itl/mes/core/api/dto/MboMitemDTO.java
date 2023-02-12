package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     物料表
 * </p>
 * @author 沧海
 */
@Data
@ApiModel(value = "MboMitemDTO",description = "物料Lov工具")
public class MboMitemDTO {
    @ApiModelProperty(value = "分页对象")
    private Page page;

    @ApiModelProperty(value = "ITEM:SITE,ITEM,VERSION【PK】")
    private String bo;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "是否为当前版本")
    private String isCurrentVersion;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "物料单位")
    private String itemUnit;

    @ApiModelProperty(value = "物料状态【M_STATUS的BO，STATE_GROUP:ITEM】")
    private String itemStateBo;

    @ApiModelProperty(value = "物料类型")
    private String itemType;

    @ApiModelProperty(value = "工艺路线【M_ROUTER的BO】")
    private String routerBo;

    @ApiModelProperty(value = "物料清单【M_BOM的BO】")
    private String bomBo;

    @ApiModelProperty(value = "默认仓库BO")
    private String warehouseBo;

    @ApiModelProperty(value = "批量数")
    private String lotSize;

    @ApiModelProperty(value = "单位BO")
    private String uomBo;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "物料属性（RA原材料、FI成品、SFI半成品）")
    private String itemAttribute;

    @ApiModelProperty(value = "管控维度（LOT批次、条码TAG、无）")
    private String controlType;

    @ApiModelProperty(value = "站点",required = true)
    private String site;

    @ApiModelProperty("创建人")
    @JsonIgnore
    private String createUser;

    @ApiModelProperty("创建时间")
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty("修改人")
    @JsonIgnore
    private String modifyUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonIgnore
    private Date modifyDate;


    @ApiModelProperty("物料bo列表")
    private List<String> itemBoList;

}
