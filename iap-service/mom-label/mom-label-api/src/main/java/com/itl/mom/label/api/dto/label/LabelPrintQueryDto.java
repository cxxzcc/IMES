package com.itl.mom.label.api.dto.label;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2021/1/25 15:19
 */
@Data
@ApiModel(value = "LabelPrintQueryDto",description = "标签打印查询请求实体")
public class LabelPrintQueryDto {

    @ApiModelProperty(value = "分页条件")
    private Page page;

    @ApiModelProperty(value = "打印日期")
    private String printDate;

    @ApiModelProperty(value = "生成日期")
    private String createDate;


    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "模糊查询code，如物料是传item,设备是传device,包装是packName,工单是shopOrder,载具是carrier")
    private String elementCode;

    @ApiModelProperty(value = "元素的类型，值为ITEM代表物料，DEVICE代表设备，PACKING代表包装，SHOP_ORDER代表工单，CARRIER代表载具")
    private String elementType;

    @ApiModelProperty(value = "表名，前端不传")
    private String tableName;

    @ApiModelProperty(value = "查询字段，前端不传")
    private String queryColumn;

}
