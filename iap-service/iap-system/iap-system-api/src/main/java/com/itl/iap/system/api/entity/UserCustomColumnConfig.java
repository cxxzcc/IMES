package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户自定义列 表实体
 */

@Data
@TableName("user_custom_column_config")
@ApiModel(value = "user_custom_column_config", description = "comments")
@Accessors(chain = true)
public class UserCustomColumnConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "BO", type = IdType.UUID)
    @ApiModelProperty(value = "唯一标识")
    private String bo;

    @TableField("USER_ID")
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @TableField("PAGE_ID")
    @ApiModelProperty(value = "页面标识")
    private String pageId;

    /*
    @TableField("SPLICE_SELECT_COLUMN_SQL")
    @ApiModelProperty(value = "拼接sql字段,可能用不到")
    private String spliceSelectColumnSql;
    */

    @TableField("SHOW_COLUMN")
    @ApiModelProperty(value = "要显示的字段集合")
    private String showColumn;

    @TableField("IS_SORT")
    @ApiModelProperty(value = "是否排序")
    private Boolean isSort;
}
