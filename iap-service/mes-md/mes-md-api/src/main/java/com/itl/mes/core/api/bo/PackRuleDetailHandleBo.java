package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
public class PackRuleDetailHandleBo implements Serializable {

    private static final String PREFIX = BOPrefixEnum.PKL.getPrefix();

    private String bo;

    private String site;

    private String packName;

    private String seq;

    public PackRuleDetailHandleBo(String bo){
        String[] split = bo.split(":")[1].split(",");
        this.bo = bo;
        this.site = split[0];
        this.packName = split[1];
        this.seq = split[2];
    }

    public PackRuleDetailHandleBo(String site, String packName, String seq){
        this.bo = new StringBuilder(PREFIX).append(":").append(site).append(",").append(packName).append(",").append(seq).toString();
        this.site = site;
        this.packName = packName;
        this.seq = seq;
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
        return packName;
    }

    public String getSeq() {
        return seq;
    }
}

