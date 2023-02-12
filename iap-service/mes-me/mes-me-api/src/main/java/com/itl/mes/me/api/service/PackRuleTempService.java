package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.bo.OrderAndCodeRuleBO;
import com.itl.mes.me.api.entity.PackRuleTemp;

import java.util.List;

/**
 * 工序-包装规则
 *
 * @author cch
 * @date 2021-06-16
 */
public interface PackRuleTempService extends IService<PackRuleTemp> {

    /**
     * 根据工序Bo查询在当前工序的包装规则, 如果为空,返回emptyList
     * @param operationBo 工序Bo
     * @return
     */
    List<PackRuleTemp> listByOperationBo(String operationBo);

    List<OrderAndCodeRuleBO> findOrderAndPackCodeRuleInfo(String sn);
}

