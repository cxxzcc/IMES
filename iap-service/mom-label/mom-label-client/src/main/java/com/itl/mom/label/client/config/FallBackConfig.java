package com.itl.mom.label.client.config;


import com.itl.mom.label.client.service.LabelService;
import com.itl.mom.label.client.service.MeProductStatusService;
import com.itl.mom.label.client.service.SnService;
import com.itl.mom.label.client.service.impl.LabelServiceImpl;
import com.itl.mom.label.client.service.impl.MeProductStatusServiceImpl;
import com.itl.mom.label.client.service.impl.SnServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Configuration
public class FallBackConfig {

    @Bean
    public LabelService labelService() {
        return new LabelServiceImpl();
    }
    @Bean
    public SnService snService() {
        return new SnServiceImpl();
    }
    @Bean
    public MeProductStatusService meProductStatusService() {
        return new MeProductStatusServiceImpl();
    }
}
