package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
@Data
@ApiModel(value = "PackRuleDetailDto",description = "包装规则明细DTO")
public class PackRuleDetailDto {

    @ApiModelProperty(value = "分页对象")
    private Page page;

    @ApiModelProperty(value="PKRuleDet:SITE,PACK_NAME【PKR】")
    private String bo;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="序号")
    private String seq;

    @ApiModelProperty(value="包装级别")
    private String packLevel;

    @ApiModelProperty(value="包装名称")
    private String packName;

    @ApiModelProperty(value="最小包装数")
    private String minQty;

    @ApiModelProperty(value="最大包装数")
    private String maxQty;

    @ApiModelProperty(value="包装规则模板bo")
    private String ruleMouldBo;

    @ApiModelProperty(value="包装规则模板")
    private String ruleMould;

    @ApiModelProperty(value="包装规则bo")
    private String ruleBo;

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

}

