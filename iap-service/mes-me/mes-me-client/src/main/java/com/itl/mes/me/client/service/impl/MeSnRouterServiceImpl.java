package com.itl.mes.me.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.UpdateSnRouteDto;
import com.itl.mes.me.api.entity.MeSnRouter;
import com.itl.mes.me.client.service.MeSnRouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dengou
 * @since JDK1.8
 */
@Component
@Slf4j
public class MeSnRouterServiceImpl implements MeSnRouterService {

    @Override
    public ResponseData addSnRoute(UpdateSnRouteDto updateSnRouteDto) {
        log.error("sorry MeSnRouterService addSnRoute feign fallback updateSnRouteDto:{} ", updateSnRouteDto);
        return ResponseData.error("新增条码工艺路线失败");
    }

    @Override
    public ResponseData<MeSnRouter> getBySn(String sn, String site) {
        log.error("sorry MeSnRouterService getBySn feign fallback sn:{}, site:{} ", sn, site);
        return ResponseData.error("根据条码查询工艺路线失败");
    }
}
