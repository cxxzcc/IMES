package com.itl.mom.label.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/4/6
 */
@Data
@ApiModel(value = "LabelPrintLogVo", description = "标签打印日志查询返回实体")
public class LabelPrintLogVo {

    @ApiModelProperty(value = "打印人")
    private String printUser;


    @ApiModelProperty(value = "打印日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date printDate;

    @ApiModelProperty(value = "根据不同的类型显示不同的code")
    private String elementCode;

    @ApiModelProperty(value = "详细条码")
    private String sn;
    @ApiModelProperty(value = "打印次数")
    private Integer printCount;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "打印机名称")
    private String printer;

}
