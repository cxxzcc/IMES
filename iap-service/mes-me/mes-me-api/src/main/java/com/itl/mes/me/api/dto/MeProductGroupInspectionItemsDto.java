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
@ApiModel(value = "MeProductGroupInspectionItemsDto",description = "产品组检验项目")
public class MeProductGroupInspectionItemsDto {

    @ApiModelProperty("工序")
    private String operationBo;
    @ApiModelProperty("物料组")
    private String itemGroupBo;
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
