package com.itl.mes.me.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 维修措施导入实体模板
 *
 * @author GKL
 * @date 2021/11/12 - 14:31
 * @since 2021/11/12 - 14:31 星期五 by GKL
 */
@Data
@ApiModel(value = "CorrectiveMaintenanceVo", description = "维修措施导入实体模板")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CorrectiveMaintenanceVo {
    /**
     * 维修措施编码
     * */
    @ApiModelProperty("维修措施编码")
    @Excel(name = "维修编码",orderNum ="1")
    private String code;
    /**
     * 维修措施标题/名称
     * */
    @ApiModelProperty("维修措施标题/名称")
    @Excel(name = "维修名称",orderNum ="2")
    private String title;
    /**
     * 维修措施说明
     * */
    @ApiModelProperty("维修措施说明")
    @Excel(name = "维修描述",orderNum ="3")
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
    @Excel(name = "维修类型",orderNum ="4")

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
    @Excel(name = "维修方法",orderNum ="6")
    private String method;
    /**
     * 维修位置
     * */
    @ApiModelProperty("维修位置")
    @Excel(name = "维修位置",orderNum ="5")
    private String place;
    /**
     * 备注
     * */
    @ApiModelProperty("备注")
    @Excel(name = "备注",orderNum ="7")
    private String remark;
    /**
     * 是否启用
     * */
    @ApiModelProperty("是否启用， Y/N")
    @Excel(name = "状态（Y启用N关闭） ", replace = {"启用_Y", "关闭_N"},orderNum ="8")
    private String isDisableFlag;
}
