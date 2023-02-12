package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itl.mes.core.api.entity.ItemPackRuleDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author FengRR
 * @date 2021/5/31
 * @since JDK1.8
 */
@Data
@ApiModel(value="ItemPackRuleVo",description="物料包装规则")
public class ItemPackRuleVo implements Serializable {

    @ApiModelProperty(value="主键")
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

    @ApiModelProperty(value="包装规则bo")
    private String ruleRuleBo;

    @ApiModelProperty(value="物料bo")
    private String itemBo;

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

