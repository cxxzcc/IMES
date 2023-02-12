package com.itl.mes.core.api.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 工艺路线 流程
 *
 **/
@Getter
@Setter
@EqualsAndHashCode
public class RouterProcessDataDTO {
    @ExcelProperty("工艺路线编码")
    private String router;
    @ExcelProperty("工序顺序码")
    private String id;
    @ExcelProperty("工序编码,版本号")
    private String operation;
    @ExcelProperty("工序参数")
    private String param;
    @ExcelProperty("下工序顺序码")
    private String nextId;
    @ExcelProperty("工序BOM")
    private String bom;
    @ExcelProperty("可否重复过站")
    private Boolean isRepeat;
    @ExcelProperty("重测次数")
    private String repeatCount;
    @ExcelProperty("可否跳站")
    private Boolean isSkip;
    @ExcelProperty("是否创建SKU")
    private Boolean isCreateSKU;

}
