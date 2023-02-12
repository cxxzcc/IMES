package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * 车间BO组装
 */
public class UomHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;
    public static final String PREFIX = BOPrefixEnum.UOM.getPrefix();

    private String bo;
    private String site;
    private String uom;

    public UomHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( "," );
        this.site = boArr[ 0 ];
        this.uom = boArr[ 1 ];
    }

    public UomHandleBO(String site, String uom){

        this.site = site;
        this.uom = uom;
        this.bo = new StringBuilder().append( PREFIX ).append( ":" ).append( site ).append( "," ).append(uom).toString();

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getUom() {
        return uom;
    }

}
