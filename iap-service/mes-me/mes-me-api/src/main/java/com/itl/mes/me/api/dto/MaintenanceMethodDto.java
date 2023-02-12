package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 维修方法dto
 * @author dengou
 * @date 2021/11/4
 */
@Data
public class MaintenanceMethodDto {

    /**
     * 主键id
     * */
    @ApiModelProperty("主键id")
    private String id;
    /**
     * 维修措施编码
     * */
    @ApiModelProperty("维修措施编码")
    private String code;
    /**
     * 维修措施标题/名称
     * */
    @ApiModelProperty("维修措施标题/名称")
    private String title;
    /**
     * 维修措施说明
     * */
    @ApiModelProperty("维修措施说明")
    private String description;
    /**
     * 异常类型id
     * */
    @ApiModelProperty("异常类型id")
    private String errorTypeId;
    /**
     * 异常类型完整id路径， 从一级父节点到叶子节点，用'-'分隔
     * */
    @ApiModelProperty("异常类型完整id路径， 从一级父节点到叶子节点，用'-'分隔")
    private String errorTypeFullIds;
    /**
     * 异常代码
     * */
    @ApiModelProperty("异常代码")
    private String errorTypeCode;
    /**
     * 异常名称
     * */
    @ApiModelProperty("异常名称")
    private String errorTypeName;
    /**
     * 异常描述
     * */
    @ApiModelProperty("异常描述")
    private String errorTypeDesc;
    /**
     * 维修方法
     * */
    @ApiModelProperty("维修方法")
    private String method;
    /**
     * 维修位置
     * */
    @ApiModelProperty("维修位置")
    private String place;
    /**
     * 备注
     * */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 是否启用
     * */
    @ApiModelProperty("是否启用， Y/N")
    private String isDisableFlag;


}
