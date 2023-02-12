package com.itl.mes.me.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.client.service.MeSnCrossStationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dengou
 * @date 2021/12/7
 */
@Component
@Slf4j
public class MeSnCrossStationLogServiceImpl implements MeSnCrossStationLogService {

    @Override
    public ResponseData<Integer> getCountBySn(String sn, String site, String operationBo) {
        log.error("sorry MeSnCrossStationLogService getCountBySn feign fallback sn:{} site:{}，operationBo：{}", sn, site, operationBo);
        return ResponseData.error("getCountBySn查询失败");
    }
}
