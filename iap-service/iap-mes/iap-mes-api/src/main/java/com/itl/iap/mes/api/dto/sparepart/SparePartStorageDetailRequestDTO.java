package com.itl.iap.mes.api.dto.sparepart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 出入库请求参数
 * @author dengou
 * @date 2021/9/23
 */
@Data
public class SparePartStorageDetailRequestDTO {


    /**
     * 仓库id
     * */
    private String wareHouseId;
    /**
     * 仓库名称
     * */
    private String wareHouseName;

    /**
     * 备件id
     * */
    private String sparePartId;
    /**
     * 单价
     * */
    private BigDecimal price;
    /**
     * 总金额
     * */
    private BigDecimal totalAmount;
    /**
     * 库存变动数量绝对值
     * */
    private Integer count;


    /**
     * 备件型号
     * */
    private String type;

    /**
     * 备件名
     * */

    private String sparePartName;


    /**
     * 库存
     * */
    private Integer inventory;

    /**
     * 备件编号
     * */
    private String sparePartNo;
}
