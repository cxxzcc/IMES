package com.itl.mes.core.api.dto;

import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@ApiModel(value = "Device", description = "设备")
@Data
@FieldNameConstants
public class DeviceConditionDto {

    @QueryWapper(value = "t.bo", queryWapperEnum = QueryWapperEnum.EQ)
    private String bo;

    @QueryWapper(value = "t.bo", queryWapperEnum = QueryWapperEnum.IN)
    private List<String> boList;

    @QueryWapper(value = "t.site",queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "工厂")
    private String site;

    @QueryWapper(value = "t.device",queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty(value = "设备编号")
    private String device;

    @QueryWapper(value = "t.DEVICE_NAME", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备名称或者名称")
    private String diviceCodeOrName;

    @QueryWapper(value = "t.DEVICE_MODEL", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @QueryWapper(value = "t.state",queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "状态")
    private String state;


}
