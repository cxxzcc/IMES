package com.itl.mom.label.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 标签转移   列表vo
 * @author dengou
 * @date 2021/10/29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelTransVo {

    /**
     * 标签id
     * */
    private String bo;

    /**
     * 条码
     * */
    private String detailCode;

    /**
     * 工单
     * */
    private String elementCode;

    /**
     * 状态
     * */
    private String state;
    private String stateDesc;

    /**
     * 是否完工
     * */
    private String isDoneFlag;

    /**
     * 是否报废
     * */
    private String isScrappedFlag;

}
