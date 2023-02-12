package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.ShopOrderPackRuleQueryDto;
import com.itl.mes.core.api.dto.ShopOrderPackRuleSaveDto;
import com.itl.mes.core.api.entity.ShopOrderPackRuleDetail;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import com.itl.mes.core.api.vo.ShopOrderPackRuleVo;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 */
public interface ShopOrderPackRuleService extends IService<ShopOrderPackRuleDetail> {

    /**
     * 工单包装规则查询
     *
     * @param shopOrderPackRuleQueryDto
     * @return
     */
    ShopOrderPackRuleVo findList(ShopOrderPackRuleQueryDto shopOrderPackRuleQueryDto);


    /**
     * 工单包装规则保存
     *
     * @param shopOrderPackRuleSaveDto
     */
    void save(ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto);

    /**
     * 根据工单Bo查询工单包装规则list, 如果为空,返回emptyList
     *
     * @param shopOrderBo
     * @return
     */
    List<ShopOrderPackRuleDetailVo> listPackRuleDetailByShopOrderBo(String shopOrderBo);

}
