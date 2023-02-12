package com.itl.mes.core.api.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class ProjectActualAllDataVO {
    @ExcelProperty("序号")
    private Integer integer;

    @ExcelProperty("仪器编码")
    private String device;

    @ExcelProperty("仪器名称")
    private String deviceName;

    @ExcelProperty("仪器位置")
    private String baseLocation;

    @ExcelProperty("项目编码")
    private String projectCode;

    @ExcelProperty("项目名称")
    private String projectName;

    @ExcelProperty("使用日期")
    private String useDate;

    @ExcelProperty("标准值")
    private BigDecimal standard;

    @ExcelProperty("测试值1")
    private BigDecimal actual1;

    @ExcelProperty("测试值2")
    private BigDecimal actual2;

    @ExcelProperty("测试值3")
    private BigDecimal actual3;

    @ExcelProperty("测试值4")
    private BigDecimal actual4;

    @ExcelProperty("测试值5")
    private BigDecimal actual5;

    @ExcelProperty("测试值6")
    private BigDecimal actual6;

    @ExcelProperty("测试值7")
    private BigDecimal actual7;

    @ExcelProperty("测试值8")
    private BigDecimal actual8;

    @ExcelProperty("测试值9")
    private BigDecimal actual9;

    @ExcelProperty("测试值10")
    private BigDecimal actual10;




}
