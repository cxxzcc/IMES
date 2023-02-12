package com.itl.mes.me.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.UpdateSnRouteDto;
import com.itl.mes.me.api.entity.MeSnRouter;
import com.itl.mes.me.client.config.FallBackConfig;
import com.itl.mes.me.client.service.impl.MeSnRouterServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author dengou
 * @date 2021/11/30
 */
@FeignClient(value = "mes-me-provider",contextId = "meSnRouter", fallback = MeSnRouterServiceImpl.class, configuration = FallBackConfig.class)
public interface MeSnRouterService {

    /**
     * 保存条码工艺路线 me_sn_route
     * */
    @PostMapping("/snRoute/add")
    ResponseData addSnRoute(@RequestBody UpdateSnRouteDto updateSnRouteDto);

    @GetMapping("/snRoute/getBySn")
    ResponseData<MeSnRouter> getBySn(@RequestParam("sn") String sn, @RequestParam("site") String site);

}
