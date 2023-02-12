package com.itl.plugins.report.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("报表详情内容")
public class ReportContentVO{

    private String id;

    @ApiModelProperty("报表编码")
    private String code;

    @ApiModelProperty("报表名称")
    private String name;

    @ApiModelProperty("预览url")
    private String previewUrl;

    @ApiModelProperty("设计url")
    private String designUrl;

    @TableField(value = "remark")
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
