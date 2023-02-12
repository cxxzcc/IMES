package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author space
 * @since 2019-06-17
 */
@RestController
@RequestMapping("/shopOrders")
@Api(tags = "工单维护功能")
public class ShopOrderController {
    private final Logger logger = LoggerFactory.getLogger(ShopOrderController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ShopOrderService shopOrderService;

    /**
     * 根据shopOrder查询
     *
     * @param shopOrder 工单
     * @return RestResult<ShopOrderFullVo>
     */
    @GetMapping("/query")
    @ApiOperation(value = "通过工单查询工单相关数据")
    @ApiImplicitParam(name = "shopOrder", value = "工单", dataType = "string", paramType = "query")
    public ResponseData<ShopOrderFullVo> getShopOrder(@RequestParam("shopOrder") String shopOrder) throws CommonException {

        return ResponseData.success(shopOrderService.getShopFullOrder(new ShopOrderHandleBO(UserUtils.getSite(), shopOrder)));
//        return ResponseData.success(shopOrderService.getShopFullOrder(new ShopOrderHandleBO("产品一组", shopOrder)));
    }

    /**
     * feign方法 根据shopOrder查询
     *
     * @param shopOrderBo 工单bo
     * @return RestResult<ShopOrderFullVo>
     */
    @GetMapping("/queryByBo")
    @ApiOperation(value = "通过工单查询工单相关数据")
    public ResponseData<ShopOrderFullVo> getShopOrderByBo(String shopOrderBo) {
        return ResponseData.success(shopOrderService.getShopFullOrder(new ShopOrderHandleBO(shopOrderBo)));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改工单")
    @ApiImplicitParam(name = "updateShopOrderFullVo", value = "修改工单", dataType = "string", paramType = "query")
    public ResponseData<ShopOrderFullVo> updateShopOrderFullVo(@RequestBody ShopOrderFullVo shopOrderFullVo) throws CommonException {
        shopOrderService.updateShopOrderFullVo(shopOrderFullVo);
        return ResponseData.success();
    }

    @PutMapping("/updateEmergenc")
    @ApiOperation(value = "修改工单紧急状态")
    @ApiImplicitParam(name = "updateEmergenc", value = "修改工单紧急状态", dataType = "string", paramType = "query")
    public ResponseData<ShopOrderFullVo> updateEmergenc(@RequestBody List<Map<String, Object>> shopOrderList) throws CommonException {
        shopOrderService.updateEmergenc(shopOrderList);
        return ResponseData.success();
    }


    @PutMapping("/updateFixedTime")
    @ApiOperation(value = "修改工单固定交期")
    @ApiImplicitParam(name = "updateFixedTime", value = "查询单条", dataType = "string", paramType = "query")
    public ResponseData updateFixedTime(@RequestParam("shopOrder") String shopOrder, @RequestParam("fixedTime") String fixedTime) throws CommonException {
        shopOrderService.updateFixedTime(shopOrder, fixedTime);
        return ResponseData.success();
    }

    /**
     * 查询所有工单
     *
     * @return RestResult<ShopOrderFullVo>
     */
    @PostMapping("/queryAllOrder")
    @ApiOperation(value = "排程查询工单相关数据")
    @ApiImplicitParam(name = "queryAllOrder", value = "工单", dataType = "string", paramType = "query")
    public ResponseData queryAllOrder(@RequestBody Map<String, Object> params, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) throws CommonException {
        return ResponseData.success(shopOrderService.getAllOrder(params, page, pageSize));
    }

    /**
     * 保存工单数据
     *
     * @param shopOrderFullVo shopOrderFullVo
     * @return RestResult<ShopOrderFullVo>
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存工单数据")
    public ResponseData<ShopOrderFullVo> saveShopOrder(@Validated @RequestBody ShopOrderFullVo shopOrderFullVo) throws CommonException {
//        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean( shopOrderFullVo ); //简单验证数据是否合规
//        if( validResult.hasErrors() ){
//            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
//        }
        shopOrderService.saveShopOrder(shopOrderFullVo);
        shopOrderFullVo = shopOrderService.getShopFullOrder(new ShopOrderHandleBO(UserUtils.getSite(), shopOrderFullVo.getShopOrder()));
        return ResponseData.success(shopOrderFullVo);
    }

    /**
     * 通过ShopOrderHandleBO查询存在的工单
     *
     * @param shopOrderHandleBO
     * @return ShopOrder
     */
    @PostMapping("/getExistShopOrder")
    @ApiOperation(value = "查询存在的工单")
    public ShopOrder getExistShopOrder(@Validated @RequestBody ShopOrderHandleBO shopOrderHandleBO) throws CommonException {
        ShopOrder shopOrder = shopOrderService.getExistShopOrder(shopOrderHandleBO);
        return shopOrder;
    }

    /**
     * 更新工单完工数量和是否完工状态
     * 工单完工数量增加 {{completeQty}} , 并判断增加之后的工单完工数量是否等于工单数量， 等于则更新工单状态为完工
     * @param completeQty 完工数量
     * @param shopOrderBo 工单bo
     * */
    @PostMapping("/updateShopOrderCompleteQtyAndState")
    public ResponseData<Boolean> updateShopOrderCompleteQtyAndState(@RequestParam String shopOrderBo, @RequestParam Integer completeQty) {
        return ResponseData.success(shopOrderService.updateShopOrderCompleteQtyAndState(shopOrderBo, completeQty));
    }

    /**
     * 删除工单数据
     *
     * @param shopOrder  工单
     * @param modifyDate 修改时间
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除工单查数据")
    @ApiImplicitParam(name = "shopOrder", value = "工单", dataType = "string", paramType = "query")
    public ResponseData<String> deleteShopOrder(String shopOrder, String modifyDate) throws CommonException {
        if (StrUtil.isBlank(shopOrder)) {
            throw new CommonException("工单不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StrUtil.isBlank(modifyDate)) {
            throw new CommonException("修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrderService.deleteShopOrderByHandleBO(new ShopOrderHandleBO(UserUtils.getSite(), shopOrder),
                DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }

    @GetMapping("/getBomComponents/{bo}")
    @ApiOperation("根据工单号获取Bom清单List<MboMitemDTO>")
    public List<MboMitemDTO> getBomComponents(@PathVariable("bo") String shopOrderBo) {
        return shopOrderService.getBomComponents(shopOrderBo);
    }

    /**
     * 更新工单标签数量
     *
     * @return RestResult<Integer>
     */
    @PostMapping("/updateLabelQty")
    @ApiOperation(value = "更新工单标签数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "bo", value = "工单BO", paramType = "query", required = true),
            @ApiImplicitParam(name = "labelQty", value = "标签数量", paramType = "query", required = true),
    })
    public ResponseData updateShopOrderLabelQtyByBO(@RequestParam("bo") String bo, @RequestParam("labelQty") BigDecimal labelQty) {
        return ResponseData.success(shopOrderService.updateShopOrderLabelQtyByBO(bo, labelQty));
    }

    /**
     * 更新工单排产数量
     *
     * @return RestResult<Integer>
     */
    @PostMapping("/updateScheduleQty")
    @ApiOperation(value = "更新工单排产数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "bo", value = "工单BO", paramType = "query", required = true),
            @ApiImplicitParam(name = "scheduleQty", value = "排产数量", paramType = "query", required = true),
    })
    public ResponseData updateShopOrderScheduleQtyByBO(@RequestParam("bo") String bo, @RequestParam("scheduleQty") BigDecimal scheduleQty) {
        return ResponseData.success(shopOrderService.updateShopOrderScheduleQtyByBO(bo, scheduleQty));
    }

    @PostMapping("/stopByStatus")
    @ApiOperation(value = "暂停状态")
    public ResponseData stopByStatus(@RequestBody ShopOrderFullVo shopOrderFullVo) throws CommonException {
        return ResponseData.success(shopOrderService.stopByStatus(shopOrderFullVo));
    }

    @PostMapping("/recoveryByStatus")
    @ApiOperation(value = "恢复状态")
    public ResponseData recoveryByStatus(@RequestBody ShopOrderFullVo shopOrderFullVo) throws CommonException {
        return ResponseData.success(shopOrderService.recoveryByStatus(shopOrderFullVo));
    }

    @PostMapping("/closeByStatus")
    @ApiOperation(value = "关闭状态")
    public ResponseData closeByStatus(@RequestBody ShopOrderFullVo shopOrderFullVo) throws CommonException {
        return ResponseData.success(shopOrderService.closeByStatus(shopOrderFullVo));
    }


}
