package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
public class PackingRuleHandleBo implements Serializable {

    private static final String PREFIX = BOPrefixEnum.PK.getPrefix();

    private String bo;

    private String site;

    private String packRule;

    public PackingRuleHandleBo(String bo){
        String[] split = bo.split(":")[1].split(",");
        this.bo = bo;
        this.site = split[0];
        this.packRule = split[1];
    }

    public PackingRuleHandleBo(String site, String packRule){
        this.site = site;
        this.packRule = packRule;
        this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(packRule).toString();
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getPackName() {
        return packRule;
    }
}

