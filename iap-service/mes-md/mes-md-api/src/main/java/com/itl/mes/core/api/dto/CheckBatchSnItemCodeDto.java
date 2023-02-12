package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 扫描物料编码[物料追溯方式]
 *
 * @author GKL
 * @date 2021/11/5 - 9:13
 * @since 2021/11/5 - 9:13 星期五 by GKL
 */
@Data
@ApiModel(value = "CheckBatchSnItemCodeDto", description = "扫描物料编码")
public class CheckBatchSnItemCodeDto {

    @ApiModelProperty(value = "物料对应的条码")
    private String itemCode;

    @ApiModelProperty(value = "bom集合数据")
    private List<CheckBatchItemCodeAndSnDto> itemBomList;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "条码对应bo")
    private String snBo;


    @ApiModelProperty(value = "工位")
    private String station;



}
