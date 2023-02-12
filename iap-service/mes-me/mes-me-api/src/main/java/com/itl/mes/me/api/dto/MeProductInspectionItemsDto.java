package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author cch
 * @date 2021/10/18$
 * @since JDK1.8
 */
@Data
@ApiModel(value = "MeProductInspectionItemsDto",description = "产品检验项目")
public class MeProductInspectionItemsDto {


    @ApiModelProperty("产品编码")
    private String itemBo;
    @ApiModelProperty("工序")
    private String operationBo;
    @ApiModelProperty("检验项目")
    private String checkProject;
    @ApiModelProperty("检验标识")
    private String checkMark;
    @ApiModelProperty("检验类型")
    private String checkType;
    @ApiModelProperty("生效日期")
    private Date effectiveDateStart;
    @ApiModelProperty("失效日期")
    private Date expiryDateStart;
    @ApiModelProperty("生效日期")
    private Date effectiveDateEnd;
    @ApiModelProperty("失效日期")
    private Date expiryDateEnd;
    private Page page;
}
