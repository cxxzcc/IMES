package com.itl.mes.me.api.vo;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 载具表
 * </p>
 *
 * @author space
 * @since 2019-07-23
 */
@Data
@ApiModel(value = "包装列表", description = "包装列表")
public class ShopOrderPackRealTreeVO {

    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "已包数量")
    private Integer count;

    private List<Tree<String>> children;


}