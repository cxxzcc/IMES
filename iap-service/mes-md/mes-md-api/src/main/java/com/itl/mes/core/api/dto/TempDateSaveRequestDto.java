package com.itl.mes.core.api.dto;

import com.itl.mes.core.api.entity.NcCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 提交暂存数据请求dto
 * @author dengou
 * @date 2021/11/15
 */
@Data
public class TempDateSaveRequestDto {

    /**
     * 工单检验项目副本id
     * me_product_inspection_items_order.id
     * */
    @ApiModelProperty(value = "工单检验项目副本id")
    private Integer id;

    /**
     * 测试值，定量检验输入
     * */
    @ApiModelProperty(name = "测试值，定量检验输入")
    private String test;

    /**
     * 检验结果
     * OK/NG
     * {@link com.itl.mes.core.api.constant.CheckResultEnum#getCode}
     * */
    @ApiModelProperty(value = "检验结果  OK/NG, 定性检验需传")
    private String result;

    /**
     * 不合格代码id
     * {@link NcCode#getBo()}
     * */
    @ApiModelProperty(value = "不合格代码bo列表")
    private List<String> ncCodeBos;

}
