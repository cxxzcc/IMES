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
@ApiModel("过站记录")
@Accessors(chain = true)
public class StationRecord {




    @ApiModelProperty("工步状态")
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("入站时间")
    private Date inTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("出站时间")
    private Date outTime;

    @ApiModelProperty("工位")
    private String stationBo;

    @ApiModelProperty("人员")
    private String username;

    @ApiModelProperty("产品")
    private String itemBo;

    @ApiModelProperty("工单")
    private String shopOrderBo;
    @ApiModelProperty("工序")
    private String operationBo;

    @ApiModelProperty("排程")
    private String scheduleNo;


}
