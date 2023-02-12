package com.itl.mes.core.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 上料清单
 *
 * @author GKL
 * @date 2021/11/29 - 14:20
 * @since 2021/11/29 - 14:20 星期一 by GKL
 */
@Data
@ApiModel(value = "UsedListDto", description = "上料清单")
public class UsedListDto {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "物料标签编码")
    private String sn;
    @ApiModelProperty(value = "物料编码")
    private String item;
    @ApiModelProperty(value = "物料名称")
    private String itemName;
    @ApiModelProperty(value = "追溯方式")
    private String zsType;
    @ApiModelProperty(value = "批量数")
    private BigDecimal lotSize;
    @ApiModelProperty(value = "剩余数量")
    private BigDecimal sysl;
    @ApiModelProperty(value = "标签状态")
    private String state;
    @ApiModelProperty(value = "上料时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    @ApiModelProperty(value = "批次")
    private String pc;

}
