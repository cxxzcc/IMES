package com.itl.iap.attachment.api.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * excel导出
 * </p>
 *
 * @author liKun
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("Excel模板sql实体")
@TableName("excel_export_general")
public class ExcelExportGeneral implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    @TableField("id")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 描述
     */
    @TableField("describe")
    @ApiModelProperty("描述")
    private String describe;

    /**
     * 导出的sql语句，一个导出模板对应一条sql
     */
    @TableField("sql")
    @ApiModelProperty(value = "查询数据所需sql",required = true)
    private String sql;

    /**
     * iap_upload_file_id表主键
     */
    @TableField("upload_file_id")
    @ApiModelProperty("iap_upload_file_id表主键")
    private String uploadFileId;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField("update_user")
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 工厂
     */
    @TableField("site")
    @ApiModelProperty("工厂")
    private String site;

    /**
     * sql状态 0启用 1停用。默认0
     */
    @TableField("status")
    @ApiModelProperty("模板状态 0启用 1停用。默认0")
    private String status;


    @TableField(exist = false)
    @ApiModelProperty("业务ID")
    private String businessId;


}
