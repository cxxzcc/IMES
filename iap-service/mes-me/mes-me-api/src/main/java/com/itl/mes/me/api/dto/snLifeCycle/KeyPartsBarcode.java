package com.itl.mes.me.api.dto.snLifeCycle;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 崔翀赫
 * @date 2021/1/28$
 * @since JDK1.8
 */
@Data
@ApiModel("基本信息")
@Accessors(chain = true)
public class KeyPartsBarcode {


    @ApiModelProperty("操作人")
    private String assyUser;
    @ApiModelProperty("装配时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assyTime;
    @ApiModelProperty("物料编码")
    private String componenetBo;
    @ApiModelProperty("关键件条码")
    private String assembledSn;
    @ApiModelProperty("用量")
    private String qty;
    @ApiModelProperty("组件类型")
    private String itemType;
    @ApiModelProperty("工位")
    private String stationBo;
}
