package com.itl.iap.mes.client.config;

import com.itl.iap.mes.client.service.ProveService;
import com.itl.iap.mes.client.service.impl.FileUploadServiceImpl;
import com.itl.iap.mes.client.service.impl.LabelServiceImpl;
import com.itl.iap.mes.client.service.impl.MesReportFeignImpl;
import com.itl.iap.mes.client.service.impl.ProveServiceImpl;
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
    public LabelServiceImpl labelService() {
        return new LabelServiceImpl();
    }
    @Bean
    public FileUploadServiceImpl fileUploadService() {
        return new FileUploadServiceImpl();
    }
    @Bean
    public MesReportFeignImpl mesReportFeign() {
        return new MesReportFeignImpl();
    }
    @Bean
    public ProveService proveService() {
        return new ProveServiceImpl();
    }
}
