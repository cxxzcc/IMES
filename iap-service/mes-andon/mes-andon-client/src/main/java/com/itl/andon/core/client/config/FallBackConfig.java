package com.itl.andon.core.client.config;

import com.itl.andon.core.client.service.AndonService;
import com.itl.andon.core.client.service.fallback.AndonServiceFallBack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FallBackConfig {


    @Bean
    public AndonService andonService() {
        return new AndonServiceFallBack();
    }

}
