package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.ItemPackRuleDetail;
import com.itl.mes.core.api.service.ItemPackRuleDetailServer;
import com.itl.mes.core.provider.mapper.ItemPackRuleDetailMapper;
import org.springframework.stereotype.Service;

/**
 * @author FengRR
 * @date 2021/5/30 ,
 * @since JDK1.8
 */
@Service
public class ItemPackRuleDetailServiceImpl extends ServiceImpl<ItemPackRuleDetailMapper, ItemPackRuleDetail>
        implements ItemPackRuleDetailServer {
}

