package com.itl.mom.label.api.dto.label;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 工单条码导入dto
 * @author dengou
 * @date 2021/11/13
 */
@Data
public class ShopOrderSnImportDto {

    /**
     * 工单编号
     * */
    @Excel(name = "工单编号")
    private String shopOrder;

    /**
     * 条码
     * */
    @Excel(name = "条码")
    private String sn;

    /**
     * 规则模板编号
     * */
    @Excel(name = "规则模板")
    private String ruleLabel;

    /**
     * 是否补打，1代表补码，2代表不补码
     * */
    @Excel(name = "是否补打")
    private Integer isComplement;

}
