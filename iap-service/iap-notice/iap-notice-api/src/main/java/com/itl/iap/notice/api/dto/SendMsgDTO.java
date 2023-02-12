package com.itl.iap.notice.api.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 发送消息vo
 *
 * @author dengou
 * @date 2021/9/26
 */
@Data
public class SendMsgDTO implements Serializable {

    /**
     * 模板code
     */
    private String code;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户id列表
     */
    private List<String> userList;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 模板参数, key-value
     */
    private Map<String, Object> params;

    /**
     * 业务id
     */
    private String businessId;
    /**
     * 业务工单编号
     */
    private String referenceOrderNo;

    public SendMsgDTO() {
    }

    @Builder
    public SendMsgDTO(String code, String userId, String userName, Map<String, Object> params,
                      String businessId, String referenceOrderNo, List<String> userList) {
        this.code = code;
        this.userId = userId;
        this.userName = userName;
        this.params = params;
        this.businessId = businessId;
        this.referenceOrderNo = referenceOrderNo;
        this.userList = userList;
    }
}
