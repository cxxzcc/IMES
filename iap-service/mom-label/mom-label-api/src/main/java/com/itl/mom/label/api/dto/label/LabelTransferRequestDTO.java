package com.itl.mom.label.api.dto.label;

import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.api.vo.ShopOrderTwoSaveVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 条码转移请求vo
 * @author dengou
 * @date 2021/10/28
 */
@Data
public class LabelTransferRequestDTO {

    /**
     * 新工单id
     * */
    @NotNull(message = "新工单bo不能为空")
    @ApiModelProperty(value = "新工单bo")
    private String newOrderNo;

    /**
     * 新工单保存对象
     * */
    @ApiModelProperty(value = "新工单保存对象")
    private ShopOrderTwoSaveVo shopOrderTwoSaveVo;

    /**
     * 新工单工艺路线
     * */
    @ApiModelProperty(value = "新工单工艺路线")
    private OrderRouter orderRouter;


    /**
     * 条码id列表
     * */
    @NotEmpty(message = "条码id列表不能为空")
    @ApiModelProperty(value = "条码id列表")
    private List<String> labelIds;

}
