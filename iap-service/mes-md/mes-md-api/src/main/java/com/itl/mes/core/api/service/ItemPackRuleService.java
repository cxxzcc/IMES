package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.ItemPackRuleDetailDto;
import com.itl.mes.core.api.entity.ItemPackRuleDetail;

import java.util.List;


/**
 * @author FengRR
 * @date 2021/6/1
 * @since JDK1.8
 */

public interface ItemPackRuleService extends IService<ItemPackRuleDetail> {

    void saveItemAndPackRule(String itemBo, List<ItemPackRuleDetailDto> itemPackRuleDetailList) throws CommonException;

    List<ItemPackRuleDetailDto> getItemAndPackRule(String item, String version);
}
