package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.TemporaryDataRetryLog;
import com.itl.mes.core.client.service.TemporaryDataRetryLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dengou
 * @date 2021/12/8
 */
@Slf4j
@Component
public class TemporaryDataRetryLogServiceImpl implements TemporaryDataRetryLogService {

    @Override
    public ResponseData<Boolean> saveLog(TemporaryDataRetryLog temporaryDataRetryLog) {
        log.error("sorry TemporaryDataRetryLogService saveLog feign fallback temporaryDataRetryLog:{}" + temporaryDataRetryLog);
        return ResponseData.error("saveLog查询失败");
    }
}
