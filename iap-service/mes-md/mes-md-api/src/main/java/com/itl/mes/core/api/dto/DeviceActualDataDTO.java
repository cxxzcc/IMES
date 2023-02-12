package com.itl.mes.core.api.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author cjq
 */
@Data
public class DeviceActualDataDTO {
    @ExcelProperty("仪器编码")
    private String device;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("使用日期")
    private Date useDate;
    @ExcelProperty("基准值")
    private String standard;


}
