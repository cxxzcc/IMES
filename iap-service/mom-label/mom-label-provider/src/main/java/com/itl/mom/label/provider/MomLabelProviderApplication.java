package com.itl.mom.label.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.servlet.MultipartConfigElement;

/**
 * TODO
 *
 * @author 汤俊
 * @date 2020-6-1 17:32
 * @since 1.0.0
 */
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.itl.mom.label.provider.mapper")
@EnableFeignClients(basePackages = {"com.itl.**"})
public class MomLabelProviderApplication {
    public static void main(String[] args) {

        SpringApplication.run(MomLabelProviderApplication.class, args);
    }

    //加入如下配置
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize(DataSize.parse("60MB"));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse("60MB"));
        return factory.createMultipartConfig();
    }


}
