package com.itl.mes.core.client.service;

import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.LabelRuleQueryServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author yx
 * @date 2021/4/7
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",
        contextId = "LabelRuleQueryService",
        fallback = LabelRuleQueryServiceImpl.class,
        configuration = FallBackConfig.class
)
public interface LabelRuleQueryService {
    /**
     * 根据字段和物料id获取各个字段的值
     * @param queryDto
     * @return
     */
    @PostMapping("/ruleLabel/getParams")
    @ApiOperation("根据字段和物料id获取各个字段的值")
    Map<String, Object> getParams(@RequestBody ItemForParamQueryDto queryDto);
}
