package com.itl.mom.label.api.vo;

import com.itl.mom.label.api.dto.label.LabelPrintSaveDto;
import com.itl.mom.label.api.entity.label.LabelPrintDetail;
import com.itl.mom.label.api.entity.label.Sn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @auth xietianzhu
 * @date 2021/4/21
 */
@Data
@ApiModel(value = "ScanReturnVo",description = "扫描条码返回的实体")
public class ScanReturnVo {

    @ApiModelProperty(value = "标签打印详细BO，主键")
    private String bo;

    @ApiModelProperty(value = "标签编码")
    private String labelCode;

    @ApiModelProperty(value = "包含的物料数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "物料BO")
    private String itemBo;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "仓库BO")
    private String warehouseBo;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "打印新增所需实体")
    private LabelPrintSaveDto labelPrintSaveDto;

    @ApiModelProperty(value = "打印明细")
    private Sn sn;

}
