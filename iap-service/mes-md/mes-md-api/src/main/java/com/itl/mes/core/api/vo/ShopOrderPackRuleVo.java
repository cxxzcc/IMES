package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 */
@Data
@ApiModel(value = "ShopOrderPackRuleVo",description = "工单包装规则查询返回实体")
public class ShopOrderPackRuleVo {

    @ApiModelProperty(value = "包装规则BO")
    private String packRuleBo;


    @ApiModelProperty(value = "包装规则")
    private String packRule;


    @ApiModelProperty(value = "工单包装规则明细信息")
    private IPage<ShopOrderPackRuleDetailVo> shopOrderPackRuleDetailVos;

}
