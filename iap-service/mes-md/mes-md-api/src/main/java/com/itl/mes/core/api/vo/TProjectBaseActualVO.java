package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("各基地项目返回对象")
public class TProjectBaseActualVO {

    @ApiModelProperty("基地编码")
    private String baseCode;

    @ApiModelProperty("基地名称")
    private String baseName;

    @ApiModelProperty("项目编码")
    private String projectCode;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date useDate;

    @ApiModelProperty("标准值")
    private BigDecimal standard;

    @ApiModelProperty("实际值")
    private BigDecimal actual;

    @ApiModelProperty("误差")
    private BigDecimal range;

    @ApiModelProperty("导入用户")
    private String createBy;

    @ApiModelProperty("导入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
