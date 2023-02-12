package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "系统参数查询对象")
public class IapSysParamDto {

    /**
     * 分页
     */
    private Page page;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 参数类型id
     */
    @ApiModelProperty(value = "参数类型id")
    private String iapSysParamTypeId;

    /**
     * 参数类型名称
     */
    @ApiModelProperty(value = "参数类型名称")
    private String iapSysParamTypeName;

    /**
     * 参数类型描述
     */
    @ApiModelProperty(value = "参数类型描述")
    private String iapSysParamTypeRemark;

    /**
     * 参数组名称
     */
    @ApiModelProperty(value = "参数组名称")
    private String name;

    /**
     * 参数组描述
     */
    @ApiModelProperty(value = "参数组描述")
    private String remark;

    /**
     * 启用状态（0：启用，1：禁用）
     */
    @ApiModelProperty(value = "启用状态（0：启用，1：禁用）")
    private Integer state;

    private String stateName;
}
