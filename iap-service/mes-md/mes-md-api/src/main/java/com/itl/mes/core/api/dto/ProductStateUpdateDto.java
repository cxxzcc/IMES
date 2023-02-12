package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 修改状态
 *
 * @author GKL
 * @date 2021/11/16 - 11:34
 * @since 2021/11/16 - 11:34 星期二 by GKL
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "productStateUpdateDto", description = "修改状态请求参数")
public class ProductStateUpdateDto {
    @ApiModelProperty(value = "productStateBo")
    private String productStateBo;
    @ApiModelProperty(value = "是否上线0:未上线，1:已上线")
    private Integer onLine;
    @ApiModelProperty(value = "当前工序名称")
    private String operationName;
    @ApiModelProperty(value = "下一个工序名称")
    private String nextOperationName;
    @ApiModelProperty(value = "工位-产线")
    private String stationsStationAndPLName;
    @ApiModelProperty(value = "采集时间")
    private Date date;
    @ApiModelProperty(value = "采集人员")
    private String operationUserName;
    @ApiModelProperty(value = "当前工序bo")
    private String operationBo;
    @ApiModelProperty(value = "下一个工序bo")
    private String nextOperationBo;

    private String snBo;

    @ApiModelProperty("是否完工, 0=否，1=是")
    private Integer done;
}
