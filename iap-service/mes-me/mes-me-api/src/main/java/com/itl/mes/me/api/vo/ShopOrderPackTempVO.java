package com.itl.mes.me.api.vo;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
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
@ApiModel(value = "工单包装临时存储", description = "工单包装临时存储")
public class ShopOrderPackTempVO {

    @ApiModelProperty(value = "工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "父包装号")
    private String packParentNo;

    @ApiModelProperty(value = "包装号")
    private String packNo;

    @ApiModelProperty(value = "包装层级")
    private String packLevel;

    @ApiModelProperty(value = "包装名称")
    private String packName;

    @ApiModelProperty(value = "最大包装数")
    private String max;


}