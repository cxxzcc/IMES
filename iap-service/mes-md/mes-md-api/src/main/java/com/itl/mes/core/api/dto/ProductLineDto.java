package com.itl.mes.core.api.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;


/**
 * @author FengRR
 * @date 2021/5/12
 * @since JDK1.8
 */

@Data
@ApiModel(value = "ProductLineDto",description = "产线查询实体")
public class ProductLineDto {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="WC:SITE,PRODUCT_LINE【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="产线【UK】")
    private String productLine;

    @ApiModelProperty(value="产线描述")
    private String productLineDesc;

    @ApiModelProperty(value="所属车间 BO【UK】")
    private String workShopBo;

    @ApiModelProperty(value="状态（已启用1，已禁用0）")
    private String state;

    @ApiModelProperty(value="建档日期")
    private Date createDate;

    @ApiModelProperty(value="建档人")
    private String createUser;

    @ApiModelProperty(value="修改日期")
    private Date modifyDate;

    @ApiModelProperty(value="修改人")
    private String modifyUser;

    @ApiModelProperty(value = "分页对象")
    private Page page;
}

