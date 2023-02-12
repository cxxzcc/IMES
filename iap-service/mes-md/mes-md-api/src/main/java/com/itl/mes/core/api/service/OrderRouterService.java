package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.api.vo.OrderRouterVo;

import java.util.List;

/**
 * <p>
 * 工艺路线-工单副本
 * </p>
 *
 * @author chenjx1
 * @since 2020-10-26
 */
public interface OrderRouterService extends IService<OrderRouter> {

    /**
     * 获取工单工艺路线集合信息(带路线图)
     *
     * @return
     */
    List<OrderRouter> getOrderRouterList(OrderRouter orderRouter) throws CommonException;

    /**
     * 获取工单工艺路线信息(带路线图)
     *
     * @return
     */
    OrderRouter getOrderRouter(String shopOrderBo) throws CommonException;


    /**
     * 获取工单工艺路线信息(带路线图)
     *
     * @return
     */
    OrderRouter getOrderRouterById(String bo) throws CommonException;

    /**
     * 保存工单工艺路线
     * */
    boolean saveOrderRouter(OrderRouterVo orderRouterVo) throws CommonException;

    /**
     * 删除工单工艺路线
     *
     * @param orderRouter 工单工艺路线
     * @throws CommonException
     */
    void deleteOrderRouter(OrderRouter orderRouter) throws CommonException;

}
