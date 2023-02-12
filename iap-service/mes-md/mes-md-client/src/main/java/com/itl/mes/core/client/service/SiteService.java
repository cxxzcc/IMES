package com.itl.mes.core.client.service;

import com.itl.mes.core.api.entity.Site;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.SiteServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author xietianzhu
 * @date 2021/3/30
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",contextId = "site", fallback = SiteServiceImpl.class, configuration = FallBackConfig.class)
public interface SiteService {

    @PostMapping("/site/getSiteBySite")
    @ApiOperation(value="通过工厂查询工厂数据")
    Site getSiteBySite(String site);

}
