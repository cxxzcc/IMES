package com.itl.mom.label.api.dto.label;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 条码在线打印Dto
 * @author dengou
 * @date 2021/12/2
 */
@Data
public class LabelInProductLinePrintDTO {


    /**
     * sn
     * */
    @ApiModelProperty(value = "条码， 在线打印")
    private String sn;

    /**
     * snList
     * */
    @ApiModelProperty(value = "条码列表， 补打")
    private List<String> snList;

    /**
     * 打印数量
     * */
    @ApiModelProperty(value = "打印数量")
    private Integer printCount;

    /**
     * 打印机名称
     * */
    @ApiModelProperty(value = "打印机名称")
    private String printer;
}
