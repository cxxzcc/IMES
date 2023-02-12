package com.itl.mom.label.api.dto.label;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 更新sn下工序
 * @author dengou
 * @date 2021/11/29
 */
@Data
public class UpdateNextProcessDTO {

    /**
     * 产品状态id列表
     * */
    @ApiModelProperty(value = "snBo列表")
    private List<String> snBoList;


    @ApiModelProperty(value = "下工序id")
    String nextProcessId;

    @ApiModelProperty(value = "下工序名称")
    String nextProcessName;

}
