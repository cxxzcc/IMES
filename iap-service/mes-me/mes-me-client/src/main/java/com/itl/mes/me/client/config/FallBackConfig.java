package com.itl.mes.me.client.config;

import com.itl.mes.me.client.service.MeProductInspectionItemsNcCodeService;
import com.itl.mes.me.client.service.MeSnCrossStationLogService;
import com.itl.mes.me.client.service.MeSnRouterService;
import com.itl.mes.me.client.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongfei
 * @date 2021/04/14
 * @since JDK1.8
 */
@Configuration
public class FallBackConfig {

    @Bean
    public LabelPrintServiceImpl labelPrintService() {
        return new LabelPrintServiceImpl();
    }

    @Bean
    public ActionServiceImpl actionService() {
        return new ActionServiceImpl();
    }
    @Bean
    public MeSnRouterService meSnRouterService() {
        return new MeSnRouterServiceImpl();
    }
    @Bean
    public MeSnCrossStationLogService meSnCrossStationLogService() {
        return new MeSnCrossStationLogServiceImpl();
    }
    @Bean
    public MeProductInspectionItemsNcCodeService meProductInspectionItemsNcCodeService() {
        return new MeProductInspectionItemsNcCodeServiceImpl();
    }

}
