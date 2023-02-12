package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yx
 * @date 2021/8/19
 * @since 1.8
 */
@TableName("m_error_type")
@Data
public class ErrorType {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    @TableField("SITE")
    private String site;
    @TableField("ERROR_CODE")
    private String errorCode;
    @TableField("ERROR_NAME")
    private String errorName;
    @TableField("ERROR_DESC")
    private String errorDesc;
    @TableField("PARENT_ID")
    private String parentId;
}
