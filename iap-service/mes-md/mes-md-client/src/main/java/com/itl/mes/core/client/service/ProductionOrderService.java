package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.ProductionOrder;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ProductionOrderServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chenjx1
 * @date 2021/11/6s
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "ProductionOrder",
        fallback = ProductionOrderServiceImpl.class,
        configuration = FallBackConfig.class)
public interface ProductionOrderService {

    /**
     * 手动保存
     * @param productionOrder 订单信息
     * */
    @PostMapping("/productionOrder/save")
    @ApiOperation(value = "手动保存生产订单")
    ResponseData<Boolean> saveOrder(@RequestBody @Valid ProductionOrder productionOrder);

    /**
     * 详情
     * @param id 订单id
     * */
    @GetMapping("/productionOrder/detail/{id}")
    @ApiOperation(value = "根据id查询生产订单详情")
    ResponseData<ProductionOrder> getDetailById(@PathVariable("id") String id);

    /**
     * 删除
     * */
    @DeleteMapping("/productionOrder/delete/{id}")
    ResponseData<Boolean> deleteById(@PathVariable("id") String id);

}
