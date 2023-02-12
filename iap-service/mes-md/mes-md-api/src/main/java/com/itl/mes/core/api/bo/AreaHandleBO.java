package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * 车间BO组装
 */
public class AreaHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;
    public static final String PREFIX = BOPrefixEnum.AREA.getPrefix();

    private String bo;
    private String site;
    private String area;

    public AreaHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.area = boArr[ 1 ];
    }

    public AreaHandleBO(String site, String area){

        this.site = site;
        this.area = area;
        this.bo = new StringBuilder().append( PREFIX ).append( ":" ).append( site ).append( "," ).append(area).toString();

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getArea() {
        return area;
    }

}
