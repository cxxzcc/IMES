package com.anjiplus.template.gaea.business;

import com.anji.plus.gaea.GaeaAutoConfiguration;
import com.anji.plus.gaea.annotation.enabled.EnabledGaeaConfiguration;
import com.anji.plus.gaea.constant.GaeaConstant;
import com.anji.plus.gaea.holder.UserContentHolder;
import com.anji.plus.gaea.holder.UserContext;
import com.anjiplus.template.gaea.business.config.CustomMetaObjectHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 业务模板
 * @author lr
 * @since 2021-02-03
 */
@EnabledGaeaConfiguration
@SpringBootApplication(scanBasePackages = {
        "com.anjiplus.template.gaea",
        "com.anji.plus"
})
@MapperScan(basePackages = {
        "com.anjiplus.template.gaea.business.modules.*.dao",
        "com.anjiplus.template.gaea.business.modules.*.**.dao",
        "com.anji.plus.gaea.*.module.*.dao"
})
public class ReportApplication {
    public static void main( String[] args ) {
        SpringApplication.run(ReportApplication.class);
    }

    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter((request, response, chain) -> {

            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                String locale = httpServletRequest.getHeader(GaeaConstant.LOCALE);
                UserContext userContext = UserContentHolder.getContext();
                //语言标识
                if (StringUtils.isNotBlank(locale)) {
                    userContext.setLocale(Locale.forLanguageTag(locale));
                }
            }
            chain.doFilter(request, response);
            //清空上下文
            UserContentHolder.clearContext();
        });
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("userOrgCodeFilter");
        registrationBean.setOrder(Integer.MIN_VALUE + 100);
        return registrationBean;
    }

    @Bean
    public CustomMetaObjectHandler customMetaObjectHandler() {
        return new CustomMetaObjectHandler();
    }
}
