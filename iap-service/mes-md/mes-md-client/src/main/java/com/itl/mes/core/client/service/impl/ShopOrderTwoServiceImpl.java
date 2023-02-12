package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import com.itl.mes.core.api.vo.ShopOrderTwoSaveVo;
import com.itl.mes.core.client.service.ShopOrderTwoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenjx1
 * @date 2021/10/26
 */
@Component
@Slf4j
public class ShopOrderTwoServiceImpl implements ShopOrderTwoService {
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
    public List<ShopOrderPackRuleDetailVo> listPackRuleDetailByShopOrderBo(String shopOrderBo) {
        log.error("sorry ShopOrder listPackRuleDetailByShopOrderBo feign fallback, shopOrderBo:{}" ,shopOrderBo);
        return null;
    }

    @Override
    public ResponseData saveShopOrder(ShopOrderTwoSaveVo shopOrderTwoSaveVo) throws CommonException {
        log.error("调用保存工单时异常");
        return ResponseData.error("调用保存工单时异常");
    }

    @Override
    public ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO) {
        return null;
    }
}
