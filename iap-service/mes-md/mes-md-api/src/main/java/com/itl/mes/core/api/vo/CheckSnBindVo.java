package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 扫描产品条码
 *
 * @author GKL
 * @date 2021/11/5 - 9:15
 * @since 2021/11/5 - 9:15 星期五 by GKL
 */
@Data
@ApiModel(value = "CheckSnBindVo", description = "扫描产品条码")
public class CheckSnBindVo {

    @ApiModelProperty(value = "sn条码对应的bo")
    private String snBo;
    @ApiModelProperty(value = "bom集合数据")
    private List<CheckSnBomVo> itemBomList;



  /*  @ApiModelProperty(value = "物料编码对应的条码")
    private List<String> itemSn;*/


}
