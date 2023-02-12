package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.PackingRuleDto;
import com.itl.mes.core.api.entity.PackingRule;

import java.util.List;

/**
 * @author FengRR
 * @date 2021/5/25
 * @since JDK1.8
 */
public interface PackingRuleService extends IService<PackingRule> {

    /**
     * 查询包装规则
     * @param packingRuleDto
     * @return
     */
    IPage<PackingRuleDto> findList(PackingRuleDto packingRuleDto) throws CommonException;

    /**
     * 查询包装规则明细
     * @param packingRuleDto
     * @return
     */
    PackingRuleDto findPackRuleDelList(PackingRuleDto packingRuleDto) throws CommonException;

    /**
     * 根据包装规则获得包装规则
     * @param rule
     * @return
     */
    PackingRuleDto findRule(String rule) throws CommonException;

    /**
     * 修改包装规则
     * @param packingRuleDto
     * @return
     */
    void updatePackingRule(PackingRuleDto packingRuleDto);

    /**
     * 删除包装规则
     * @param bos
     * @return
     */
    void deletePackingRule(List<String> bos);

    /**
     * 新增包装规则
     * @param packingRuleDto
     * @return
     */
    void addPackingRule(PackingRuleDto packingRuleDto) throws CommonException;

    /**
     * 保存包装规则
     * @param packingRuleDto
     * @return
     */
    void savePackingRule(PackingRuleDto packingRuleDto) throws CommonException;

    /**
     * 删除包装规则明细
     * @param bos
     * @return
     */
    void deletePackingRuleDel(List<String> bos);

    /**
     * 根据包装规则BO获得包装规则
     * @param bo
     * @return
     */
    String getPackRule(String bo);

    /**
     * 根据包装规则BO获得包装规则信息
     * @param packRuleHandleBO
     * @return
     */
    PackingRule getPackRuleByPackRuleHandleBO(String packRuleHandleBO);


}
