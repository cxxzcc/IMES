package com.itl.mes.core.api.bo;


import com.itl.mes.core.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * 工单工艺路线BO组装
 */
public class OrderRouterHandleBO implements Serializable {

    public static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.ROUTER.getPrefix();

    private String bo;
    private String site;
    private String router;
    private String version;
    private String shopOrderBo;

    public OrderRouterHandleBO(String bo ){

        this.bo = bo;
        String[] boArr = bo.substring( PREFIX.length()+1 ).split( ",", -1 );
        this.site = boArr[ 0 ];
        this.router = boArr[ 1 ];
        this.version = boArr[ 2 ];
        this.shopOrderBo = boArr[ 3 ];
    }

    public OrderRouterHandleBO(String site, String router, String version, String shopOrderBo){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( router ).append( "," ).append( version ).append( "," ).append( shopOrderBo ).toString();
        this.site = site;
        this.router = router;
        this.version = version;
        this.shopOrderBo = shopOrderBo;

    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getRouter() {
        return router;
    }

    public String getVersion() {
        return version;
    }

    public String getShopOrderBo() {
        return shopOrderBo;
    }
}
