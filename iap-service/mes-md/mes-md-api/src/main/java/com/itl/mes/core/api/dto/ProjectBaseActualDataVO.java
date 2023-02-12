package com.itl.mes.core.api.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author cjq
 */
@Data
public class ProjectBaseActualDataVO {
    @ExcelProperty("基地编码")
    private String baseCode;

    @ExcelProperty("基地名称")
    private String baseName;

    @ExcelProperty("项目编码")
    private String projectCode;

    @ExcelProperty("项目名称")
    private String projectName;

    @ExcelProperty("使用日期")
    private String useDate;

    @ExcelProperty("标准值")
    private BigDecimal standard;

    @ExcelProperty("实际值")
    private BigDecimal actual;

    @ExcelProperty("误差")
    private BigDecimal range;

    @ExcelProperty("导入用户")
    private String createBy;

    @ExcelProperty("导入时间")
    private String createTime;


}
