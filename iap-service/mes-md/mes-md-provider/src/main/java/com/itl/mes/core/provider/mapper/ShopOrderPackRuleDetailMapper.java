package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.ShopOrderPackRuleQueryDto;
import com.itl.mes.core.api.entity.ShopOrderPackRuleDetail;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 */
@Repository
public interface ShopOrderPackRuleDetailMapper extends BaseMapper<ShopOrderPackRuleDetail> {


    /**
     * 根据包装规则BO查询包装规则明细集合
     * @return
     */
    IPage<ShopOrderPackRuleDetailVo> findListByRuleBo(Page page, @Param("shopOrderPackRuleQueryDto") ShopOrderPackRuleQueryDto shopOrderPackRuleQueryDto);


    /**
     * 根据包装规则BO和工单BO查询工单包装规则明细
     * @return
     */
    IPage<ShopOrderPackRuleDetailVo> findListByShopOrderBo(Page page,@Param("shopOrderPackRuleQueryDto") ShopOrderPackRuleQueryDto shopOrderPackRuleQueryDto);

}
