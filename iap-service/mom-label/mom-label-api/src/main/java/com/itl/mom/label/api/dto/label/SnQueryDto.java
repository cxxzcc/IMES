package com.itl.mom.label.api.dto.label;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author liuchenghao
 * @date 2021/1/26 14:00
 */
@Data
@ApiModel(value = "SnQueryDto", description = "标签打印详细信息查询实体")
@Accessors(chain = true)
public class SnQueryDto {

    @ApiModelProperty(value = "分页条件")
    private Page page;

    @ApiModelProperty(value = "标签打印BO,主键")
    private String labelPrintBo;


    @ApiModelProperty(value = "元素的类型，值为ITEM代表物料，DEVICE代表设备，PACKING代表包装，SHOP_ORDER代表工单，CARRIER代表载具" , required = true)
    private String elementType;

    @ApiModelProperty(value = "表名，前端不传")
    private String tableName;

    @ApiModelProperty(value = "查询字段，前端不传")
    private String queryColumn;

    @ApiModelProperty(value = "元素 for log")
    private String element;
    @ApiModelProperty(value = "sn for log")
    private String sn;
    @ApiModelProperty(value = "状态 0=关闭， 1=正常")
    private String state;
    @ApiModelProperty(value = "printUser for log")
    private String printUser;
    @ApiModelProperty(value = "printDateStart for log")
    private Date printDateStart;
    @ApiModelProperty(value = "printDateEnd for log")
    private Date printDateEnd;


    @ApiModelProperty(value = "currentOperation for status")
    private String currentOperation;
    @ApiModelProperty(value = "nextOperation for status")
    private String nextOperation;
    @ApiModelProperty(value = "产线 for status")
    private String currentPlStation;
    @ApiModelProperty(value = "done for status")
    private String done;
    @ApiModelProperty(value = "onLine for status")
    private String onLine;

    private String site;


    @ApiModelProperty(value = "是否挂起, 0=否，1=是")
    private String isHold;

    @ApiModelProperty(value = "是否报废, 0=否，1=是")
    private String isScrapped;
    @ApiModelProperty(value = "工位")
    private String station;
    @ApiModelProperty(value = "类型列表")
    private List<String> typeList;


}
