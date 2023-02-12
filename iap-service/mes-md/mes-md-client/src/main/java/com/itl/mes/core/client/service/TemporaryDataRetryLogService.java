package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.TemporaryDataRetryLog;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.TemporaryDataRetryLogServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 过站重传日志feign服务
 * @author dengou
 * @date 2021/12/8
 */
@FeignClient(value = "mes-md-provider",contextId = "temporaryDataRetryLog", fallback = TemporaryDataRetryLogServiceImpl.class, configuration = FallBackConfig.class)
public interface TemporaryDataRetryLogService {

    /**
     * feign 保存重传记录
     * */
    @PostMapping("/temporary/retryLog/save")
    ResponseData<Boolean> saveLog(@RequestBody TemporaryDataRetryLog temporaryDataRetryLog);

}
