package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 异常数量统计dto
 * @author dengou
 * @date 2021/10/20
 */
public class AbnormalCountStatisticsDTO {
    /**
     * 状态
     * */
    @ApiModelProperty("状态code")
    private String state;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    /**
     * 状态说明
     * */
    @ApiModelProperty("状态说明")
    private String stateDesc;

    /**
     * 数量
     * */
    @ApiModelProperty("数量")
    private Integer count;


}
