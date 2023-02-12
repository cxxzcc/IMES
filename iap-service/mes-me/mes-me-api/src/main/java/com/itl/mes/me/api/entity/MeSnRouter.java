package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 条码-工艺路线表
 * </p>
 *
 * @author dengou
 * @since 2021-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MeSnRouter对象", description="条码-工艺路线表")
@TableName("me_sn_router")
public class MeSnRouter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "条码")
    @TableId(value = "sn", type = IdType.INPUT)
    private String sn;

    @ApiModelProperty(value = "工艺路线")
    private String json;

    @ApiModelProperty(value = "当前步骤")
    @TableField(value = "[current]")
    private String current;

    @ApiModelProperty(value = "更新人用户名")
    private String updateUser;
    @ApiModelProperty(value = "更新人时间")
    private Date updateTime;
    @ApiModelProperty(value = "版本号，更新产生")
    private String version;
    @ApiModelProperty(value = "工厂id")
    private String site;


}
