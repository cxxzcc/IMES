package com.itl.mes.core.client.service.impl;

import com.itl.mes.core.api.entity.Site;
import com.itl.mes.core.client.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author xietianzhu
 * @date 2021/3/30
 * @since JDK1.8
 */
@Slf4j
@Component
public class SiteServiceImpl implements SiteService {
    @Override
    public Site getSiteBySite(String site) {
        log.error("sorry SiteServiceImpl getSiteBySite feign fallback " );
        return null;
    }

}
