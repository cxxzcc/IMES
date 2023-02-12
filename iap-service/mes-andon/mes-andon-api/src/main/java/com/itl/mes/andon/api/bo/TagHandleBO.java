package com.itl.mes.andon.api.bo;

import com.itl.mes.andon.api.constant.BOPrefixEnum;

public class TagHandleBO {
    private static final String PREFIX = BOPrefixEnum.ATT.getPrefix();

    private String bo;
    private String site;
    private String andonTypeTag;


    public TagHandleBO(String bo) {
        this.bo = bo;
        String[] split = bo.split(":")[1].split(",");
        this.site = split[0];
        this.andonTypeTag = split[1];
    }

    public TagHandleBO(String site, String andonTypeTag) {
        this.bo = new StringBuilder(PREFIX).append(":").append(site)
                .append(",").append(andonTypeTag)
                .toString();
        this.site = site;
        this.andonTypeTag = andonTypeTag;
    }
    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getAndon() {
        return andonTypeTag;
    }

}
