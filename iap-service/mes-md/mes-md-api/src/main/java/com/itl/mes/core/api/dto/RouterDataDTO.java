package com.itl.mes.core.api.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 工艺路线
 *
 **/
@Getter
@Setter
@EqualsAndHashCode
public class RouterDataDTO {
    @ExcelProperty("工艺路线编码")
    private String router;
    @ExcelProperty("工艺路线名称")
    private String routerName;
    @ExcelProperty("工艺路线描述")
    private String routerDesc;
    @ExcelProperty("路线类型")
    private String routerType;
    @ExcelProperty("路线状态")
    private String state;
    @ExcelProperty("版本")
    private String version;

}
