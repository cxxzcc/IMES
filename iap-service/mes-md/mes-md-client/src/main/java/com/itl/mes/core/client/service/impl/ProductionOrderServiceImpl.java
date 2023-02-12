package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.ProductionOrder;
import com.itl.mes.core.client.service.ProductionOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenjx1
 * @date 2021/11/6
 * @since JDK1.8
 */
@Slf4j
@Component
public class ProductionOrderServiceImpl implements ProductionOrderService {

    @Override
    public ResponseData<Boolean> saveOrder(ProductionOrder productionOrder) {
        log.error("调用手动保存生产订单失败！");
        return ResponseData.error("调用手动保存生产订单失败！");
    }

    @Override
    public ResponseData<ProductionOrder> getDetailById(String id) {
        log.error("根据id调用查询生产订单详情失败！");
        return ResponseData.error("根据id调用查询生产订单详情失败！");
    }

    @Override
    public ResponseData<Boolean> deleteById(String id) {
        log.error("调用生产订单删除失败！");
        return ResponseData.error("调用生产订单删除失败！");
    }
}
