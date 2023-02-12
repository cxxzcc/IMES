package com.itl.iap.system.api.bo;

import com.itl.iap.system.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * User_Custom_Column_Config 用户自定义列配置表的BO生成
 */
public class UserCustomColumnConfigBo implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PREFIX = BOPrefixEnum.USER_CUSTOM_CONFIG.getPrefix();

    private String bo;
    private String userId;
    private String pageId;

    public UserCustomColumnConfigBo(String bo) {
        this.bo = bo;
        String[] boArr = bo.substring(PREFIX.length() + 1).split(",");
        this.userId = boArr[0];
        this.pageId = boArr[1];
    }

    public UserCustomColumnConfigBo(String userId, String pageId) {
        this.userId = userId;
        this.pageId = pageId;
        this.bo = PREFIX + ":" + userId + "," + pageId;
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public String getBo() {
        return bo;
    }

    public String getUserId() {
        return userId;
    }

    public String getPageId() {
        return pageId;
    }
}
