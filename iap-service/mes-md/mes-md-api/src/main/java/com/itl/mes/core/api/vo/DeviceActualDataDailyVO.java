package com.itl.mes.core.api.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class DeviceActualDataDailyVO {
    @ExcelProperty("仪器位置")
    private String baseLocation;

    @ExcelProperty("仪器编码")
    private String device;

    @ExcelProperty("基准值")
    private BigDecimal standard;

    @ExcelProperty("测试值1")
    private BigDecimal actual1;

    @ExcelProperty("误差")
    private String range;

    @ExcelProperty("结果")
    private String result;




}
