package com.itl.mes.andon.api.dto;

import lombok.Data;

/**
 * 安灯触发-保存维修工单回调 参数
 * @author dengou
 * @date 2021/11/19
 */
@Data
public class AndonSaveRepairCallBackDTO {


    /**
     * 安灯bo
     * */
    private String andonBo;

    /**
     * 维修单号
     * */
    private String repairNo;

}
