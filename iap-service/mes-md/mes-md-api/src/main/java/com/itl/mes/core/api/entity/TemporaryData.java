package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_temporary_data")
@ApiModel(value="TemporaryData对象", description="")
public class TemporaryData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(type=IdType.UUID)
    private String id;

    @ApiModelProperty(value = "条形码")
    private String sn;

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "暂存数据")
    private String content;

    /**
     * 暂存数据类型
     * {@link TemporaryDataTypeEnum#getCode()}
     * */
    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人用户名")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人用户名")
    private String updateUser;


}
