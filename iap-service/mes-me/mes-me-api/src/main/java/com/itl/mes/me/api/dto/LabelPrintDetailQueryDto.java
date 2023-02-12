package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2021/1/26 14:00
 */
@Data
@ApiModel(value = "LabelPrintDetailQueryDto",description = "标签打印详细信息查询实体")
public class LabelPrintDetailQueryDto {

    @ApiModelProperty(value = "分页条件")
    private Page page;

    @ApiModelProperty(value = "标签打印BO,主键")
    private String labelPrintBo;


    @ApiModelProperty(value = "元素的类型，值为ITEM代表物料，DEVICE代表设备，PACKING代表包装，SHOP_ORDER代表工单，CARRIER代表载具")
    private String elementType;

    @ApiModelProperty(value = "表名，前端不传")
    private String tableName;

    @ApiModelProperty(value = "查询字段，前端不传")
    private String queryColumn;

}
