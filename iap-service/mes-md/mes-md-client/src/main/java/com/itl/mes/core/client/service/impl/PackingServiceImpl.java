package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.client.service.PackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhongfei
 * @date 2021/04/26
 * @since JDK1.8
 */
@Slf4j
@Component
public class PackingServiceImpl implements PackingService {

    @Override
    public ResponseData<PackLevel> getPackLevelByBo(String bo) {
        log.error("sorry PackingService getPackLevelByBo feign fallback bo:{}" + bo);
        return ResponseData.error("查询包装明细信息失败");
    }
}
