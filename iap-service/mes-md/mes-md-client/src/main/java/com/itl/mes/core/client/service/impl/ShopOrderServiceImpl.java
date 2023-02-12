package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import com.itl.mes.core.client.service.ShopOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author space
 * @date 2021/4/13
 * @since JDK1.8
 */
@Component
@Slf4j
public class ShopOrderServiceImpl implements ShopOrderService {
    @Override
    public List<MboMitemDTO> getBomComponents(String shopOrderBo) {
        log.error("sorry ShopOrder getBomComponents feign fallback, shopOrderBo:{}" + shopOrderBo);
        return null;
    }

    /**
     * 更新工单标签数量
     *
     * @param bo
     * @param labelQty
     * @return RestResult<Integer>
     */
    @Override
    public ResponseData updateShopOrderLabelQtyByBO(String bo, BigDecimal labelQty) {
        log.error("sorry ShopOrder updateShopOrderLabelQtyByBO feign fallback, bo:{} ,labelQty:{}" ,bo,labelQty);
        return ResponseData.error("调用更新工单标签数量失败");
    }

    /**
     * 更新工单排产数量
     *
     * @param bo
     * @param scheduleQty
     * @return RestResult<Integer>
     */
    @Override
    public ResponseData updateShopOrderScheduleQtyByBO(String bo, BigDecimal scheduleQty) {
        log.error("sorry ShopOrder updateShopOrderScheduleQtyByBO feign fallback, bo:{} ,scheduleQty:{}" ,bo,scheduleQty);
        return ResponseData.error("调用更新工单排产数量失败");
    }

    /**
     * 根据shopOrder查询
     *
     * @param shopOrder 工单
     * @return RestResult<ShopOrderFullVo>
     */
    @Override
    public ResponseData<ShopOrderFullVo> getShopOrder(String shopOrder) {
        log.error("sorry ShopOrder getShopOrder feign fallback, shopOrder:{}" ,shopOrder);
        return ResponseData.error("根据shopOrder查询失败");
    }

    @Override
    public ResponseData<ShopOrderFullVo> getShopOrderByBo(String shopOrderBo) {
        log.error("sorry ShopOrder getShopOrderByBo feign fallback, shopOrderBo:{}" ,shopOrderBo);
        return ResponseData.error("根据shopOrderBo查询失败");
    }

    @Override
    public List<ShopOrderPackRuleDetailVo> listPackRuleDetailByShopOrderBo(String shopOrderBo) {
        log.error("sorry ShopOrder listPackRuleDetailByShopOrderBo feign fallback, shopOrderBo:{}" ,shopOrderBo);
        return null;
    }

    @Override
    public ResponseData<ShopOrderFullVo> saveShopOrder(ShopOrderFullVo shopOrderFullVo) {
        return null;
    }

    @Override
    public ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO) {
        return null;
    }

    @Override
    public ResponseData<Boolean> updateShopOrderCompleteQtyAndState(String shopOrderBo, Integer completeQty) {
        log.error("sorry ShopOrder updateShopOrderCompleteQtyAndState feign fallback, shopOrderBo:{},completeQty:{}" , shopOrderBo, completeQty);
        return ResponseData.error("更新工单完工数量和状态查询失败");
    }
}
