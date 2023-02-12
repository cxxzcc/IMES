package com.itl.iap.notice.api.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lK
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("push_target_person")
@ApiModel
public class PushTargetPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty("主键")
    private String id;

    /**
     * 目标人id(用户id)
     */
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 模板id (msg_public_template表)
     */
    @TableField("template_id")
    @ApiModelProperty("模板id")
    private String templateId;

}
