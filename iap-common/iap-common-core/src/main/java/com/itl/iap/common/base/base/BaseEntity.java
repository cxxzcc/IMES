package com.itl.iap.common.base.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author cjq
 * @Date 2021/10/18 11:35 上午
 * @Description TODO
 */
@Data
@FieldNameConstants
public class BaseEntity {

    @ApiModelProperty("创建人")
    @TableField(value = "createBy", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    @TableField(value = "updateBy", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("修改时间")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
