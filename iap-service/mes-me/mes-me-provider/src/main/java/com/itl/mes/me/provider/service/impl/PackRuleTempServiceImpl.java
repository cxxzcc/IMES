package com.itl.mes.me.provider.service.impl;

import com.itl.mes.me.api.bo.OrderAndCodeRuleBO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itl.mes.me.provider.mapper.PackRuleTempMapper;
import com.itl.mes.me.api.entity.PackRuleTemp;
import com.itl.mes.me.api.service.PackRuleTempService;

import java.util.Collections;
import java.util.List;

/**
 * 工序-包装规则
 *
 * @author cch
 * @date 2021-06-16
 */
@Service
public class PackRuleTempServiceImpl extends ServiceImpl<PackRuleTempMapper, PackRuleTemp> implements PackRuleTempService {

    @Override
    public List<PackRuleTemp> listByOperationBo(String operationBo) {
        List<PackRuleTemp> list = baseMapper.listByOperationBo(operationBo);
        if (list == null || list.size() == 0) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<OrderAndCodeRuleBO> findOrderAndPackCodeRuleInfo(String sn) {
        return baseMapper.findOrderAndPackCodeRuleInfo(sn);
    }
}
