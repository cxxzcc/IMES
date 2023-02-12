package com.itl.mes.core.api.dto;

import com.itl.mes.core.api.entity.ShopOrderBomComponnet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/5/27
 */
@Data
@ApiModel(value = "ShopOrderBomComponnetSaveDto",description = "工单的工单BOM/工序BOM保存实体")
public class ShopOrderBomComponnetSaveDto {

    @ApiModelProperty(value = "bomBo")
    private String bomBo;

    @ApiModelProperty(value = "工单BO，前端不传")
    private String shopOrderBo;

    @ApiModelProperty(value = "类型:SO代表工单，OP代表工序")
    private String type;

    @Valid
    @ApiModelProperty(value = "工单BOM保存实体")
    private List<ShopOrderBomComponnet> shopOrderBomComponnets;

}
