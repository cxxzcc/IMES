package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 崔翀赫
 * @date 2021/1/14$
 * @since JDK1.8
 */
@Data
@ApiModel(value = "BomDto",description = "Device查询实体")
public class DeviceDto {
    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "设备编号")
    private String device;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "车间")
    private String workShop;
    private String site;
}
