package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.PackRuleDetail;
import com.itl.mes.core.api.service.PackingRuleDetailService;
import com.itl.mes.core.provider.mapper.PackingRuleDetailMapper;
import org.springframework.stereotype.Service;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
@Service
public class PackingRuleDetailServiceImpl extends ServiceImpl<PackingRuleDetailMapper , PackRuleDetail>
        implements PackingRuleDetailService{
}

