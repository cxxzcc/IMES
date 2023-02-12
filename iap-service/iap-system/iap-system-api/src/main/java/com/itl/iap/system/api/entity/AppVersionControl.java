package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 版本控制表实体类
 *
 * @author zhancen
 * @date 2021-10-26
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AppVersionControl", description = "app版本控制表")
@TableName("app_version_control")
public class AppVersionControl extends BaseEntity implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    @TableField("versionCode")
    private String versionCode;

    @ApiModelProperty(value = "版本名称")
    @TableField("versionName")
    private String versionName;

    @ApiModelProperty(value = "版本信息")
    @TableField("versionInfo")
    private String versionInfo;

    /**
     * 下载链接
     */
    @ApiModelProperty(value = "下载链接")
    @TableField("downloadUrl")
    private String downloadUrl;

    /**
     * 是否最新版本(0否1是)
     */
    @ApiModelProperty(value = "是否最新版本(0否1是)")
    @TableField("isUpToDate")
    private String isUpToDate;

    @ApiModelProperty(value = "是否最新版本(0否1是)")
    @TableField(exist = false)
    private String isUpToDateName;


    @ApiModelProperty(value = "是否强制更新 1是0否")
    @TableField("forceUpdate")
    private String forceUpdate;

    @ApiModelProperty(value = "是否强制更新 1是0否")
    @TableField(exist = false)
    private String forceUpdateName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    /**
     * 版本新增内容日志
     */
    @ApiModelProperty(value = "版本新增内容日志")
    @TableField("newContentLog")
    private String newContentLog;

    /**
     * 版本修改内容日志
     */
    @ApiModelProperty(value = "版本修改内容日志")
    @TableField("fixedContentLog")
    private String fixedContentLog;


    /**
     * 最新APP版本下载链接
     */
    @ApiModelProperty(value = "最新APP版本下载链接")
    @TableField(exist = false)
    private String latestDownloadLink;

    /**
     * site
     */
    @ApiModelProperty(value = "平台 1101 安卓 1102ios")
    @TableField(value = "systemType")
    private String systemType;


}
