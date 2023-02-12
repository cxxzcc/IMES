package com.itl.iap.attachment.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * description:
 * author: lK
 * time: 2021/7/7 15:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("Excel模板sql及模板文件信息Vo")
public class ExcelExportFileVo implements Serializable {

    @ApiModelProperty("模板sql表的id")
    private String id;
    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件类型(模板文件后缀)")
    private String fileType;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createDate;
    /**
     * 创建者
     */
    @ApiModelProperty("创建人")
    private String creater;
    /**
     * 最后修改人
     */
    @ApiModelProperty("最后修改人")
    private String lastUpdateBy;
    /**
     * 最后修改时间
     */
    @ApiModelProperty("最后修改时间")
    private Date lastUpdateDate;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 状态 （1被删除 0 正常）
     */
//    @TableField("on_type")
//    private Short onType;
}
