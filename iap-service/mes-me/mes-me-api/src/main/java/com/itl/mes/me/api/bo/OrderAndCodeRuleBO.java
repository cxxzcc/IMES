package com.itl.mes.me.api.bo;

import lombok.Data;

@Data
public class OrderAndCodeRuleBO {
    /*条码*/
    private String sn;
    /*工单bo*/
    private String shopOrderBo;
    /*工单*/
    private String shopOrder;
    /*物料bo*/
    private String itemBo;
    /*物料编码*/
    private String item;
    /*物料名称*/
    private String itemName;
    /*包装bo*/
    private String packBo;
    /*包装名称*/
    private String packName;
    /*包装级别*/
    private String packLevel;
    /*最小*/
    private String minQty;
    /*最大*/
    private String maxQty;
    /*已完成数*/
    private String completeCount;
    /*编码规则*/
    private String codeRuleBo;
}
