package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2021/5/27
 */
@Data
@ApiModel(value = "ShopOrderBomComponnetQueryDto",description = "工单BOM组件查询请求实体")
public class ShopOrderBomComponnetQueryDto {

    @ApiModelProperty(value = "分页参数")
    private Page page;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "类型:SO代表工单，OP代表工序")
    private String type;

    @ApiModelProperty(value = "bomBo，新建时使用")
    private String bomBo;

    @ApiModelProperty(value = "工单Bo,编辑时使用")
    private String shopOrderBo;



}
