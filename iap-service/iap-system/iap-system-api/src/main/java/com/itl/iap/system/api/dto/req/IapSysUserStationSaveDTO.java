package com.itl.iap.system.api.dto.req;

import com.itl.iap.system.api.entity.IapSysUserStationT;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/18
 */
@ApiModel(value = "IapSysUserStationSaveDTO",description = "用户分配工位保存实体")
@Data
public class IapSysUserStationSaveDTO {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "工位Bo")
    private List<String> stationBos;

    @ApiModelProperty(value = "工位集合")
    private List<IapSysUserStationT> iapSysUserStationTList;

}
