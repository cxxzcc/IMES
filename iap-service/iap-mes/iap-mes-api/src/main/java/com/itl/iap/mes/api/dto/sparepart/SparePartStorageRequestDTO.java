package com.itl.iap.mes.api.dto.sparepart;

import lombok.Data;

import java.util.List;

/**
 * 入出库请求参数vo
 * @author dengou
 * @date 2021/9/23
 */
@Data
public class SparePartStorageRequestDTO {

    /**
     * 工单类型
     * */
    private String type;
    /**
     * 经办人
     * */
    private String agent;
    /**
     * 关联单号, 比如出库关联维修单
     * */
    private String referenceOrderNo;
    /**
     * 部门id
     * */
    private String organizationId;

    /**
     * 出库/入库标识, true=入库，false=出库
     * */
    private Boolean isIn;

    /**
     * 备件数量详情
     * */
    private List<SparePartStorageDetailRequestDTO> details;


}
