package com.itl.iap.notice.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author tanq
 * @version 1.0
 * @date 2020/10/27
 * @JDK 1.8
 * @description 消息中心启动类
 */
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.itl.iap.notice.provider.mapper")
@EnableFeignClients(basePackages = {"com.itl.**"})
public class IapNoticeProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(IapNoticeProviderApplication.class, args);
        System.out.println("启动成功");
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //这里处理编码问题 请求微信接口时不指定编码 返回的昵称 地区 等信息可能会乱码
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            if (messageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) messageConverter).setDefaultCharset(StandardCharsets.UTF_8);
            }
            if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) messageConverter).setDefaultCharset(StandardCharsets.UTF_8);
            }
            if (messageConverter instanceof AllEncompassingFormHttpMessageConverter) {
                ((AllEncompassingFormHttpMessageConverter) messageConverter).setCharset(StandardCharsets.UTF_8);
            }
        }
        return restTemplate;
    }
}
