package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @auth chenjx1
 * @date 2021/12/07
 */
@Data
@ApiModel(value = "StationVo",description = "查询用户工位数据")
public class IapSysUserStationVO implements Serializable {

    @ApiModelProperty(value="用户ID")
    private String userId;

    @ApiModelProperty(value="工位BO")
    private String bo;

    @ApiModelProperty(value="工位编号")
    private String station;

    @ApiModelProperty(value="工位名称")
    private String stationName;

    @ApiModelProperty(value="工位描述")
    private String stationDesc;

    @ApiModelProperty(value="默认工位")
    //@EnumValue(intValues = {1, 0}, message = "默认工位参数只能是0或1")
    private int isDefault;

    @ApiModelProperty(value="工位序号")
    private int serialNum;

    @ApiModelProperty(value="工厂编号")
    private String site;
}
