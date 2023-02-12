package com.itl.iap.notice.api.service;


import java.util.Map;

/**
 * 发送通知服务类
 *
 * @author 曾慧任
 * @date  2020-06-29
 * @since jdk1.8
 */
public interface NoticeService {
    /**
     *  发送消息
     * @param notice
     */
    void sendMessage(Map<String, Object> notice);

    /**
     * 更改状态为已读
     *
     * @param id
     */
    void updateReadFlag(String id);

    Map getMsgSendRecordListByUser(String userId, Integer page , Integer pageSize);

    /**
     * 获取用户未读消息数量
     * @param userId 用户id
     * */
    Integer getUnreadCountByUser(String userId);

    /**
     * 未读消息数量根据消息类型分组
     * @param userId 用户id
     * */
    Map<String, Object> getUnreadCountGroupByType(String userId);
}
