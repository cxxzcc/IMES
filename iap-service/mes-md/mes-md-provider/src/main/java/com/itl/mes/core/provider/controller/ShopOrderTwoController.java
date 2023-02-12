package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetSaveDto;
import com.itl.mes.core.api.entity.OrderRouterProcess;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.service.ShopOrderTwoService;
import com.itl.mes.core.api.vo.ShopOrderTwoAsSaveVo;
import com.itl.mes.core.api.vo.ShopOrderTwoFullVo;
import com.itl.mes.core.api.vo.ShopOrderTwoSaveVo;
import com.itl.mom.label.client.service.SnService;
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
 *
 * @author chenjx1
 * @since 2021-10-26
 */
@RestController
@RequestMapping("/shopOrderTwo")
@Api(tags = "工单维护功能Two" )
public class ShopOrderTwoController {
    private final Logger logger = LoggerFactory.getLogger(ShopOrderTwoController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ShopOrderTwoService shopOrderTwoService;

    @Autowired
    private SnService snService;


    /**
    * 根据shopOrder查询
    *
    * @param shopOrder 工单
    * @return RestResult<ShopOrderTwoFullVo>
    */
    @GetMapping("/query")
    @ApiOperation(value="通过工单查询工单相关数据")
    @ApiImplicitParam(name="shopOrder",value="工单",dataType="string", paramType = "query")
    public ResponseData<ShopOrderTwoFullVo> getShopOrder(@RequestParam("shopOrder") String shopOrder ) throws CommonException {
        return ResponseData.success(shopOrderTwoService.getShopTwoFullOrder( new ShopOrderHandleBO( UserUtils.getSite(), shopOrder ) ));
    }

    /**
     * 获取工序BOM
     * @param shopOrderBo
     * @param orderRouterProcess
     * @return
     * @throws CommonException
     */
    @PostMapping("/getShopOrderBomComponnetSaveDto")
    @ApiOperation(value="获取工序BOM")
    @ApiImplicitParam(name="orderRouterProcess",value="获取工序BOM")
    public ResponseData getShopOrderBomComponnetSaveDto(@RequestParam("shopOrderBo") String shopOrderBo, @RequestBody OrderRouterProcess orderRouterProcess) throws CommonException {
        ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = shopOrderTwoService.getShopOrderBomComponnetSaveDto(shopOrderBo, orderRouterProcess);
        return ResponseData.success(shopOrderBomComponnetSaveDto);
    }

    @PutMapping("/update")
    //@ApiOperation(value="修改工单")
    //@ApiImplicitParam(name="updateShopOrderTwoFullVo",value="修改工单",dataType="string", paramType = "query")
    public ResponseData<ShopOrderTwoFullVo> updateShopOrderTwoFullVo(@RequestBody ShopOrderTwoFullVo shopOrderTwoFullVo) throws CommonException {
        shopOrderTwoService.updateShopOrderTwoFullVo( shopOrderTwoFullVo);
        return ResponseData.success();
    }

    @PutMapping("/updateEmergenc")
    //@ApiOperation(value="修改工单紧急状态")
    //@ApiImplicitParam(name="updateEmergenc",value="修改工单紧急状态",dataType="string", paramType = "query")
    public ResponseData<ShopOrderTwoFullVo> updateEmergenc(@RequestBody List<Map<String, Object>> shopOrderList) throws CommonException {
        shopOrderTwoService.updateEmergenc(shopOrderList);
        return ResponseData.success();
    }


    @PutMapping("/updateFixedTime")
    //@ApiOperation(value="修改工单固定交期")
    //@ApiImplicitParam(name="updateFixedTime",value="查询单条",dataType="string", paramType = "query")
    public ResponseData updateFixedTime(@RequestParam("shopOrder") String shopOrder, @RequestParam("fixedTime") String fixedTime) throws CommonException {
        shopOrderTwoService.updateFixedTime(shopOrder, fixedTime);
        return ResponseData.success();
    }

    /**
     * 查询所有工单
     *
     * @return RestResult<ShopOrderTwoFullVo>
     */
    @PostMapping("/queryAllOrder")
    @ApiOperation(value="排程查询工单相关数据")
    @ApiImplicitParam(name="queryAllOrder",value="查询全部工单",dataType="string", paramType = "query")
    public ResponseData queryAllOrder(@RequestBody Map<String, Object> params, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) throws CommonException {
        return ResponseData.success(shopOrderTwoService.getAllOrder(params, page, pageSize));
    }

    /**
     * 保存工单数据
     *
     * @param shopOrderTwoSaveVo shopOrderTwoFullVo
     * @return RestResult<ShopOrderTwoFullVo>
     */
    @PostMapping("/save")
    @ApiOperation(value="保存工单数据")
    public ResponseData saveShopOrder(@Validated @RequestBody ShopOrderTwoSaveVo shopOrderTwoSaveVo ) throws CommonException {
        shopOrderTwoService.saveShopOrder( shopOrderTwoSaveVo );
        // ShopOrderTwoFullVo shopOrderTwoFullVo = shopOrderTwoService.getShopTwoFullOrder( new ShopOrderHandleBO( UserUtils.getSite(),shopOrderTwoSaveVo.getShopOrder() ) );
        return ResponseData.success(true);
    }

    /**
     * 保存拆工单数据
     *
     * @param shopOrderTwoAsSaveVo
     * @return RestResult<ShopOrderTwoFullVo>
     */
    /*@PostMapping("/saveAs")
    @ApiOperation(value="保存拆工单数据")
    public ResponseData<ShopOrderTwoFullVo> saveAsShopOrder(@Validated @RequestBody ShopOrderTwoSaveVo shopOrderTwoSaveVo ) throws CommonException {
        shopOrderTwoService.saveAsShopOrder( shopOrderTwoSaveVo );
        ShopOrderTwoFullVo shopOrderTwoFullVo = shopOrderTwoService.getShopTwoFullOrder( new ShopOrderHandleBO( UserUtils.getSite(),shopOrderTwoSaveVo.getShopOrder() ) );
        return ResponseData.success(shopOrderTwoFullVo);
    }*/
    @PostMapping("/saveAs")
    @ApiOperation(value="保存拆工单数据")
    public ResponseData<ShopOrderTwoFullVo> saveAsShopOrder(@Validated @RequestBody ShopOrderTwoAsSaveVo shopOrderTwoAsSaveVo ) {
        shopOrderTwoService.saveAsShopOrder( shopOrderTwoAsSaveVo );
        String shopOrderStr = shopOrderTwoAsSaveVo.getShopOrder();
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),shopOrderStr);

        // 旧工单排程数量变更
        String shopOrderBoStr = shopOrderHandleBO.getBo();
        int schedulQtyOld = 0;
        ResponseData<List<String>> responseDataSnBoOnLineList = snService.queryOrderBoList(shopOrderBoStr,1);
        Assert.valid(!responseDataSnBoOnLineList.isSuccess(), responseDataSnBoOnLineList.getMsg());
        List<String> snBoOnLineList = responseDataSnBoOnLineList.getData();
        if(snBoOnLineList != null && !"".equals(snBoOnLineList)){
            schedulQtyOld = snBoOnLineList.size();
        }
        ShopOrderTwoFullVo shopOrderTwoFullVo = shopOrderTwoService.getShopTwoFullOrder(shopOrderHandleBO);
        //int schedulQtyOldSum = -(shopOrderTwoFullVo.getSchedulQty().intValue() - schedulQtyOld);
        shopOrderTwoService.updateShopOrderScheduleQtyAndOrderQtyByBO(shopOrderHandleBO.getBo(), new BigDecimal(schedulQtyOld), new BigDecimal(schedulQtyOld));
        /*ShopOrder shopOrder = new ShopOrder();
        shopOrder.setBo(shopOrderBoStr);
        shopOrder.setSchedulQty(new BigDecimal(schedulQtyOld));
        shopOrder.setOrderQty(new BigDecimal(schedulQtyOld));
        //shopOrder.setLabelQty(new BigDecimal(schedulQtyOld));
        shopOrderTwoService.updateById(shopOrder);*/


        //ShopOrderTwoFullVo shopOrderTwoFullVo = shopOrderTwoService.getShopTwoFullOrder(shopOrderHandleBO);
        return ResponseData.success(shopOrderTwoFullVo);
    }

    /**
     * 通过ShopOrderHandleBO查询存在的工单
     * @param shopOrderHandleBO
     * @return ShopOrder
     */
    @PostMapping("/getExistShopOrder")
    @ApiOperation(value="查询存在的工单")
    public ShopOrder getExistShopOrder(@Validated @RequestBody ShopOrderHandleBO shopOrderHandleBO) throws CommonException {
        ShopOrder shopOrder = shopOrderTwoService.getExistShopOrder(shopOrderHandleBO);
        return shopOrder;
    }

    /**
     * 删除工单数据
     *
     * @param shopOrder 工单
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value="删除工单查数据")
    @ApiImplicitParam(name="shopOrder",value="工单",dataType="string", paramType = "query")
    public ResponseData<String> deleteShopOrder( String shopOrder) throws CommonException {
        if( StrUtil.isBlank( shopOrder ) ){
            throw new CommonException( "工单不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        /*if( StrUtil.isBlank( modifyDate ) ){
            throw new CommonException("修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }*/
        /*shopOrderTwoService.deleteShopOrderByHandleBO( new ShopOrderHandleBO( UserUtils.getSite(),shopOrder ),
                DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ) );*/
        shopOrderTwoService.deleteShopOrderByHandleBO( new ShopOrderHandleBO( UserUtils.getSite(),shopOrder ) );
        return ResponseData.success( "success" );
    }

    @GetMapping("/getBomComponents/{bo}")
    //@ApiOperation("根据工单号获取Bom清单List<MboMitemDTO>")
    public List<MboMitemDTO> getBomComponents(@PathVariable("bo") String shopOrderBo) {
        return shopOrderTwoService.getBomComponents(shopOrderBo);
    }

    /**
     * 更新工单标签数量
     *
     * @return RestResult<Integer>
     */
    @PostMapping("/updateLabelQty")
    /*@ApiOperation(value="更新工单标签数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="bo", value = "工单BO", paramType = "query", required = true),
            @ApiImplicitParam(name = "labelQty",value = "标签数量", paramType = "query", required = true),
    })*/
    public ResponseData updateShopOrderLabelQtyByBO(@RequestParam("bo") String bo,@RequestParam("labelQty") BigDecimal labelQty) {
        return ResponseData.success(shopOrderTwoService.updateShopOrderLabelQtyByBO(bo,labelQty));
    }

    /**
     * 更新工单排产数量
     *
     * @return RestResult<Integer>
     */
    @PostMapping("/updateScheduleQty")
    /*@ApiOperation(value="更新工单排产数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="bo", value = "工单BO", paramType = "query", required = true),
            @ApiImplicitParam(name = "scheduleQty",value = "排产数量", paramType = "query", required = true),
    })*/
    public ResponseData updateShopOrderScheduleQtyByBO(@RequestParam("bo") String bo,@RequestParam("scheduleQty") BigDecimal scheduleQty) {
        return ResponseData.success(shopOrderTwoService.updateShopOrderScheduleQtyByBO(bo,scheduleQty));
    }

    /**
     * 更新工单排产、工单数量
     *
     * @return RestResult<Integer>
     */
    @PostMapping("/updateScheduleQtyAndOrderQty")
    /*@ApiOperation(value="更新工单排产数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="bo", value = "工单BO", paramType = "query", required = true),
            @ApiImplicitParam(name = "scheduleQty",value = "排产数量", paramType = "query", required = true),
    })*/
    public ResponseData updateShopOrderScheduleQtyAndOrderQtyByBO(@RequestParam("bo") String bo,@RequestParam("scheduleQty") BigDecimal scheduleQty, @RequestParam("orderQty") BigDecimal orderQty) {
        return ResponseData.success(shopOrderTwoService.updateShopOrderScheduleQtyAndOrderQtyByBO(bo,scheduleQty,orderQty));
    }

    @PostMapping("/stopByStatus")
    //@ApiOperation(value="暂停状态")
    public ResponseData stopByStatus (@RequestBody ShopOrderTwoFullVo shopOrderTwoFullVo) throws CommonException {
        return  ResponseData.success(shopOrderTwoService.stopByStatus(shopOrderTwoFullVo));
    }

    @PostMapping("/recoveryByStatus")
    //@ApiOperation(value="恢复状态")
    public ResponseData recoveryByStatus (@RequestBody ShopOrderTwoFullVo shopOrderTwoFullVo) throws CommonException {
        return  ResponseData.success(shopOrderTwoService.recoveryByStatus(shopOrderTwoFullVo));
    }

    @PostMapping("/closeByStatus")
    //@ApiOperation(value="关闭状态")
    public ResponseData closeByStatus (@RequestBody ShopOrderTwoFullVo shopOrderTwoFullVo) throws CommonException {
        return  ResponseData.success(shopOrderTwoService.closeByStatus(shopOrderTwoFullVo));
    }

    @PostMapping("/updateOrderProductInspectionItems")
    @ApiOperation(value="更新工单检验项目副本")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="itemBo", value = "产品物料ID", paramType = "query", required = true),
            @ApiImplicitParam(name = "orderBo",value = "工单ID", paramType = "query", required = true),
    })
    public Boolean updateOrderProductInspectionItems(@RequestParam String itemBo, @RequestParam String orderBo) throws CommonException {
        return shopOrderTwoService.updateOrderProductInspectionItems(itemBo, orderBo);
    }


}
