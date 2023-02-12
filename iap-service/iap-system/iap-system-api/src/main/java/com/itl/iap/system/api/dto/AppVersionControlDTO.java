package com.itl.iap.system.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "AppVersionControl", description = "app版本")
public class AppVersionControlDTO {

    @ApiModelProperty(value = "设置新版本时填")
    private String id;

    @NotBlank(message = "版本号不能为空")
    @ApiModelProperty(value = "版本号")
    private String versionCode;

    @ApiModelProperty(value = "版本名称")
    private String versionName;

    @ApiModelProperty(value = "版本信息")
    private String versionInfo;

    @NotBlank(message = "下载链接不能为空")
    @ApiModelProperty(value = "下载链接")
    private String downloadUrl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "版本新增内容日志")
    private String newContentLog;

    @ApiModelProperty(value = "版本修改内容日志")
    private String fixedContentLog;

    @NotBlank(message = "平台类型不能为空")
    @ApiModelProperty(value = "平台 1101 安卓 1102 ios")
    private String systemType;

    @ApiModelProperty(value = "是否强制更新 1 是 0 否")
    private String forceUpdate;


}
