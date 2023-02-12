package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sky,
 * @date 2019/6/17
 * @time 13:56
 */
@Data
@ApiModel(value = "DeviceTypeVo", description = "保存设备类型数据使用")
public class DeviceTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="编号【UK】")
    private String deviceType;

    @ApiModelProperty(value="名称")
    private String deviceTypeName;

    @ApiModelProperty(value="描述")
    private String deviceTypeDesc;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty("分配的故障代码")
    private List<String> faultIds;

}
