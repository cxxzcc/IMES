package com.itl.iap.notice.provider.core.qywx.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author cjq
 * @Date 2021/10/8 3:26 下午
 * @Description TODO
 */
@Data
public class WeChatData {
    /**
     * 应用id
     */
    private Integer agentid;
    /**
     * 发送者
     */
    private String touser;
    /**
     * 消息类型
     */
    private String msgtype;

    /**
     * 消息内容
     */
    private Object text;

}
