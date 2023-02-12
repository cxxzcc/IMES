package com.itl.mes.core.provider.feign.fallback;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.provider.feign.CustomerFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 供应商调用fallback
 * @author dengou
 * @date 2021/9/29
 */
@Component
@Slf4j
public class CustomerFeignServiceImpl implements CustomerFeignService {

    @Override
    public ResponseData<List<Map<String, Object>>> getByIds(List<String> ids) {
        log.error("sorry CustomerFeignService getByIds feign fallback returnBos: {}", ids);
        return ResponseData.error("getByIdsd调用失败");
    }
}
