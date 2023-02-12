package com.itl.mes.core.client.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import com.itl.mes.core.api.vo.ShopOrderTwoSaveVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ShopOrderTwoServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenjx1
 * @date 2021/10/26
 */
@FeignClient(value = "mes-md-provider",contextId = "ShopOrderTwo",
        fallback = ShopOrderTwoServiceImpl.class,
        configuration = FallBackConfig.class)
public interface ShopOrderTwoService {
    @GetMapping("/shopOrderTwo/getBomComponents/{bo}")
    @ApiOperation("根据工单号获取Bom清单List<MboMitemDTO>")
    List<MboMitemDTO> getBomComponents(@PathVariable("bo") String shopOrderBo);

    /**
     * 更新工单标签数量
     *
     * @return RestResult<Integer>
     */
    @PostMapping("/shopOrderTwo/updateLabelQty")
    @ApiOperation(value="更新工单标签数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="bo", value = "工单BO", paramType = "query", required = true),
            @ApiImplicitParam(name = "labelQty",value = "标签数量", paramType = "query", required = true),
    })
    ResponseData updateShopOrderLabelQtyByBO(@RequestParam("bo") String bo, @RequestParam("labelQty") BigDecimal labelQty);

    /**
     * 更新工单排产数量
     *
     * @return RestResult<Integer>
     */
    @PostMapping("/shopOrderTwo/updateScheduleQty")
    @ApiOperation(value="更新工单排产数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="bo", value = "工单BO", paramType = "query", required = true),
            @ApiImplicitParam(name = "scheduleQty",value = "排产数量", paramType = "query", required = true),
    })
    ResponseData updateShopOrderScheduleQtyByBO(@RequestParam("bo") String bo, @RequestParam("scheduleQty") BigDecimal scheduleQty);


    /**
     * 根据shopOrder查询
     *
     * @param shopOrder 工单
     * @return RestResult<ShopOrderFullVo>
     */
    @GetMapping("/shopOrderTwo/query")
    @ApiOperation(value="通过工单查询工单相关数据")
    @ApiImplicitParam(name="shopOrder",value="工单",dataType="string", paramType = "query")
    ResponseData<ShopOrderFullVo> getShopOrder(@RequestParam("shopOrder") String shopOrder );

    @GetMapping("/shopOrderPackRule/listPackRuleDetailByShopOrderBo")
    @ApiOperation(value = "listPackRuleDetailByShopOrderBo for feign")
    List<ShopOrderPackRuleDetailVo> listPackRuleDetailByShopOrderBo(@RequestParam("shopOrderBo") String shopOrderBo);

    /**
     * 排程下达时保存工单数据
     * @param shopOrderTwoSaveVo shopOrderTwoSaveVo
     * @return RestResult<ShopOrderFullVo>
     */
    // @RequestMapping(value = "/shopOrderTwo/save",method = RequestMethod.POST)
    @PostMapping("/shopOrderTwo/save")
    @ApiOperation(value="保存工单数据")
    ResponseData saveShopOrder(@Validated @RequestBody ShopOrderTwoSaveVo shopOrderTwoSaveVo ) throws CommonException;

    /**
     * 通过ShopOrderHandleBO查询存在的工单
     * @param shopOrderHandleBO
     * @return ShopOrder
     */
    @PostMapping("/shopOrderTwo/getExistShopOrder")
    @ApiOperation(value="查询存在的工单")
    ShopOrder getExistShopOrder(@Validated @RequestBody ShopOrderHandleBO shopOrderHandleBO);

}
