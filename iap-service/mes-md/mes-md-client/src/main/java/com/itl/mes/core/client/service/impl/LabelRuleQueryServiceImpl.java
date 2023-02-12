package com.itl.mes.core.client.service.impl;

import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.client.service.LabelRuleQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yx
 * @date 2021/4/7
 * @since JDK1.8
 */
@Slf4j
@Service
public class LabelRuleQueryServiceImpl implements LabelRuleQueryService {
    @Override
    public Map<String, Object> getParams(ItemForParamQueryDto queryDto) {
        log.error("sorry LabelRuleQueryServiceImpl getParams feign fallback queryDto:{}" + queryDto.toString());
        return null;
    }
}
