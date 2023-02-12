package com.itl.plugins.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_report_content")
@ApiModel("报表详情内容")
public class ReportContent extends BaseEntity {

    @TableId(type = IdType.UUID)
    @TableField(value = "id")
    private String id;

    @TableField(value = "code")
    @ApiModelProperty("报表编码")
    private String code;

    @TableField(value = "name")
    @ApiModelProperty("报表名称")
    private String name;

    @TableField(value = "fileName")
    @ApiModelProperty("报表文件名称")
    private String fileName;

    @TableField(value = "content")
    @JsonIgnore
    @ApiModelProperty("内容")
    private String content;

    @TableField(value = "previewUrl")
    @ApiModelProperty("预览url")
    private String previewUrl;

    @TableField(value = "designUrl")
    @ApiModelProperty("设计url")
    private String designUrl;

    @TableField(value = "remark")
    @ApiModelProperty("备注")
    private String remark;

    @TableField(value = "isDelete")
    @ApiModelProperty("是否删除 1 是 0否")
    @JsonIgnore
    private String isDelete;

}
