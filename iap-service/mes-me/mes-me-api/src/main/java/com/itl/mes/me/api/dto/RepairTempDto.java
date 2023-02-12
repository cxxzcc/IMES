package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 维修暂存dto
 * @author dengou
 * @date 2021/11/22
 */
@Data
public class RepairTempDto {


    /**
     * 不合格记录id
     * */
    @ApiModelProperty(value = "不合格记录id")
    private String defectRecordId;

    /**
     * 维修措施id
     * */
    @ApiModelProperty(value = "维修措施id")
    private String repairMethodId;


}
