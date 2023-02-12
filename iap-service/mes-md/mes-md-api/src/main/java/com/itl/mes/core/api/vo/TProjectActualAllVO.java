package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("交叉验证")
public class TProjectActualAllVO {

    @ApiModelProperty("项目交叉验证数据")
    private List<TProjectActualVO> list;

    @ApiModelProperty("均值")
    private List<BigDecimal> average;

    @ApiModelProperty("仪器间极差")
    private List<BigDecimal> range;

    @ApiModelProperty("与基准器相比偏差")
    private List<BigDecimal> offset;

    @ApiModelProperty("结果")
    private List<String> result;

}
