package com.itl.mes.core.api.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author cjq
 */
@Data
public class ProjectActualDataDTO {
    @ExcelProperty("仪器编码")
    private String device;
    @ExcelProperty("项目编码")
    private String projectCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("使用日期")
    private Date useDate;
    @ExcelProperty("标准值")
    private String standard;


}
