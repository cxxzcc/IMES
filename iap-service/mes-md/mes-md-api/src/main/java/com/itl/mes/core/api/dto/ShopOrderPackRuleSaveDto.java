package com.itl.mes.core.api.dto;

import com.itl.mes.core.api.entity.ShopOrderPackRuleDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 */
@Data
@ApiModel(value = "ShopOrderPackRuleSaveDto",description = "工单包装规则保存请求实体")
public class ShopOrderPackRuleSaveDto {

    @ApiModelProperty(value = "工单BO")
    private String shopOrderBo;

    @ApiModelProperty(value = "包装规则BO")
    private String packRuleBo;

    @ApiModelProperty(value = "物料BO")
    private String itemBo;

    @Valid
    @ApiModelProperty(value = "工单包装规则明细实体")
    private List<ShopOrderPackRuleDetail> shopOrderPackRuleDetails;

}
