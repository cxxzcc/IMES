package com.itl.mes.core.api.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
@Data
@ApiModel(value = "PackingRuleDto",description = "包装规则查询dto")
public class PackingRuleDto {

    @ApiModelProperty(value = "分页对象")
    private Page page;

    @ApiModelProperty(value="PKR:SITE,PACK_NAME【PKR】")
    private String bo;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="包装规则")
    private String packRule;

    @ApiModelProperty(value="包装规则描述")
    private String packRuleDesc;

    @ApiModelProperty(value="物料类型 1-原材料 2-半成品 3-成品")
    private String itemType;

    @ApiModelProperty(value="状态  0-禁用 1-启用")
    private String status;

    @ApiModelProperty(value="创建人")
    private String createUser;

    @ApiModelProperty(value="创建时间")
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value="修改人")
    private String modifyUser;

    @ApiModelProperty(value="修改时间")
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyDate;

    @ApiModelProperty( value="包装规则细节" )
    private List<PackRuleDetailDto> packRuleDetailDtoList;
}

