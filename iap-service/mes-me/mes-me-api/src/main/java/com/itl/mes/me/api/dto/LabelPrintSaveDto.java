package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuchenghao
 * @date 2021/1/25 15:16
 */
@Data
@ApiModel(value = "LabelPrintSaveDto",description = "标签打印保存请求实体")
public class LabelPrintSaveDto {


    @ApiModelProperty(value = "规则模板BO")
    private String ruleLabelBo;

    @ApiModelProperty(value = "规则模板参数，为还未匹配数据的参数")
    private String ruleLabelParams;

    @ApiModelProperty(value = "标签参数，用于生成pdf文件")
    private String labelParams;

    @ApiModelProperty(value = "是否补码，1代表补码，2代表不补码")
    private Integer isComplement;

    @ApiModelProperty(value = "条码数量")
    private Integer barCodeAmount;

    @ApiModelProperty(value = "模板类型")
    private String templateType;

    @ApiModelProperty(value = "lodop的代码段")
    private String lodopText;

    @ApiModelProperty(value = "元素的类型，值为ITEM代表物料，DEVICE代表设备，PACKING代表包装，SHOP_ORDER代表工单，CARRIER代表载具")
    private String elementType;

    @ApiModelProperty(value = "元素BO，根据类型的不同传其各自的BO")
    private String elementBo;

    @ApiModelProperty(value = "包装的最大数")
    private BigDecimal maxQty;

}
