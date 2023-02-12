package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 */
@Data
@ApiModel(value = "ShopOrderPackRuleQueryDto",description = "工单包装规则查询请求实体")
public class ShopOrderPackRuleQueryDto {

    @ApiModelProperty(value = "分页条件")
    private Page page;


    @ApiModelProperty(value = "物料BO")
    private String itemBo;


    @ApiModelProperty(value = "包装规则BO，前端不传")
    private String ruleBo;

    @ApiModelProperty(value = "工单BO")
    private String shopOrderBo;

}
