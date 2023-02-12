package com.itl.mes.me.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.client.config.FallBackConfig;
import com.itl.mes.me.client.service.impl.MeSnCrossStationLogServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author dengou
 * @date 2021/12/7
 */
@FeignClient(value = "mes-me-provider",contextId = "meSnCrossStationLog", fallback = MeSnCrossStationLogServiceImpl.class, configuration = FallBackConfig.class)
public interface MeSnCrossStationLogService {

    /**
     * 获取过站次数
     * */
    @GetMapping("/snCrossStationLog/countBySn")
    ResponseData<Integer> getCountBySn(@RequestParam("sn") String sn, @RequestParam("site") String site, @RequestParam("operationBo") String operationBo);
}
