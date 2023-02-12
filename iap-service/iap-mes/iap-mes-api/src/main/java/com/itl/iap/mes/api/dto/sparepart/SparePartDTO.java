package com.itl.iap.mes.api.dto.sparepart;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 备件信息
 * @author dengou
 * @date 2021/9/17
 */
@Data
public class SparePartDTO implements Serializable {

    /**
     * id
     * */
    @ApiModelProperty(value="主键")
    private String id;
    /**
     * 备件编号
     * */
    @ApiModelProperty(value="备件编号")
    @Excel(name = "备件编号", orderNum = "1")
    private String sparePartNo;

    /**
     * 备件名称
     * */
    @ApiModelProperty(value="备件名称")
    @Excel(name = "备件名称", orderNum = "3")
    private String name;

    /**
     * 备件类型
     * */
    @ApiModelProperty(value="备件类型")
    private String type;
    /**
     * 备件类型名称
     * */
    @ApiModelProperty(value="备件类型名称")
    @Excel(name = "备件类型", orderNum = "4")
    private String typeDesc;

    /**
     * 供应商
     * */
    @ApiModelProperty(value="供应商")
    private String supplier;
    /**
     * 供应商名称
     * */
    @ApiModelProperty(value="供应商名称")
    @Excel(name = "供应商", orderNum = "5")
    private String supplierName;

    /**
     * 当前库存（总库存）
     * */
    @ApiModelProperty(value="当前库存")
    @Excel(name = "总库存", orderNum = "8")
    private Integer currentInventory;
    /**
     * 库存上限
     * */
    @ApiModelProperty(value="库存上限")
    @Excel(name = "库存上限", orderNum = "6")
    private Integer inventoryMax;
    /**
     * 库存下限
     * */
    @ApiModelProperty(value="库存下限")
    @Excel(name = "库存下限", orderNum = "7")
    private Integer inventoryMin;
    /**
     * 计量单位
     * */
    @ApiModelProperty(value="计量单位")
    @Excel(name = "计量单位", orderNum = "9")
    private String unit;

    /**
     * 封面图。取第一张
     * */
    @ApiModelProperty(value="封面图(取新增时候的第一张图)")
   // @Excel(name = "封面图", type = 2 ,width = 40 , imageType = 1, orderNum = "2")
    private String coverImg;

    /**
     * 仓库id
     * */
    @ApiModelProperty(value="仓库id")
    private String wareHouseId;
    /**
     * 仓库名
     * */
    @ApiModelProperty(value="仓库名")
    private String wareHouseName;

    /**
     * 规格型号
     * */
    @ApiModelProperty(value="规格型号")
    private String spec;

    /**
     * 唯一key， 值=id_wareHouseId
     * */
    @ApiModelProperty(value="唯一key, lov组件需要")
    private String unionKey;

    public String getUnionKey() {
        return id + "_" + wareHouseId;
    }
}
