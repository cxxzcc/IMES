package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ProductionOrder;

import java.util.Map;

/**
 * 生产订单service
 * @author dengou
 * @date 2021/10/11
 */
public interface ProductionOrderService extends IService<ProductionOrder> {

    /**
     * 保存订单
     * @param productionOrder 订单信息
     * @return 是否成功
     * */
    Boolean saveOrder(ProductionOrder productionOrder);

    /**
     * 详情
     * @param id 订单id
     * @return 订单详情
     * */
    ProductionOrder detailById(String id);
    /**
     * 详情
     * @param orderNo 订单编号
     * @return 订单详情
     * */
    ProductionOrder detailByOrderNo(String orderNo);
    /**
     * 查询列表
     * @param params 分页查询参数
     * @return 分页列表
     * */
    Page<ProductionOrder> queryPage(Map<String, Object> params);

    /**
     * 根据id删除订单， 只有未下达状态可以删除
     * @param id 订单编号
     * */
    Boolean deleteById(String id);
}
