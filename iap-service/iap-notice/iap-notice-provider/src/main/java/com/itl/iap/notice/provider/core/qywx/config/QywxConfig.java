package com.itl.iap.notice.provider.core.qywx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author cjq
 * @Date 2021/10/9 11:19 上午
 * @Description TODO
 */
@Data
@Component
@ConfigurationProperties(prefix = "qywx")
public class QywxConfig {

    /**
     * 企业ID
     */
    private String corpid;
    /**
     * 应用密钥
     */
    private String corpsecret;
    /**
     * 应用id
     */
    private Integer agentid;


    public String getGetTokenUrl() {
        return "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;
    }

    public String getSendMessageUrl() {
        return "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
    }

    public String getUserIdByMobile(){
        return "https://qyapi.weixin.qq.com/cgi-bin/user/getuserid?access_token=";
    }


}
