package com.itl.plugins.report;

import com.bstek.ureport.console.UReportServlet;
import com.itl.iap.common.base.config.CommonMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:context.xml")
@MapperScan("com.itl.plugins.report.mapper")
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.itl.**"})
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean ureportServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean();
        bean.setServlet(new UReportServlet());
        bean.addUrlMappings("/ureport/*");
        return bean;
    }

    @Bean
    public CommonMetaObjectHandler commonMetaObjectHandler() {
        return new CommonMetaObjectHandler();
    }


}
