package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.StringUtils;
import com.itl.mes.core.api.constant.ProductionOrderSourceEnum;
import com.itl.mes.core.api.entity.ProductionOrder;
import com.itl.mes.core.api.service.ProductionOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 生产订单controller
 * @author dengou
 * @date 2021/10/11
 */
@RestController
@RequestMapping("/productionOrder")
@Api(value = "生产订单")
public class ProductionOrderController {

    @Autowired
    private ProductionOrderService productionOrderService;


    /**
     * 手动保存
     * @param productionOrder 订单信息
     * */
    @PostMapping("/save")
    @ApiOperation(value = "手动保存生产订单")
    public ResponseData<Boolean> saveOrder(@RequestBody @Valid ProductionOrder productionOrder) {
        productionOrder.setSource(ProductionOrderSourceEnum.MES.getCode());
        return ResponseData.success(productionOrderService.saveOrder(productionOrder));
    }

    /**
     * 排程下达时调用修改生产订单
     * @param productionOrder 订单信息
     * */
    @PostMapping("/scheduleSave")
    @ApiOperation(value = "排程下达时调用修改生产订单")
    public ResponseData<Boolean> scheduleSaveOrder(@RequestBody @Valid ProductionOrder productionOrder) {
        return ResponseData.success(productionOrderService.saveOrder(productionOrder));
    }

    /**
     * erp导入
     * @param productionOrder 订单信息
     * */
    @PostMapping("/erp/save")
    @ApiOperation(value = "erp导入生产订单")
    public ResponseData<Boolean> saveOrderByErp(@RequestBody @Valid ProductionOrder productionOrder) {
        productionOrder.setSource(ProductionOrderSourceEnum.ERP.getCode());
        return ResponseData.success(productionOrderService.saveOrder(productionOrder));
    }

    /**
     * 查询列表
     * @param params 分页查询参数
     * */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询生产订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="orderNo", value = "订单编号，支持模糊查询。" ),
            @ApiImplicitParam( name="itemNo", value = "物料编号，支持模糊查询。" ),
            @ApiImplicitParam( name="itemName", value = "物料名称，支持模糊查询。" ),
            @ApiImplicitParam( name="status", value = "订单状态,字典:PRO_ORD_STATUS" ),
            @ApiImplicitParam( name="type", value = "订单类型,字典:PRO_ORD_TYPE" ),
            @ApiImplicitParam( name="sellOrderNo", value = "销售订单编号，支持模糊查询。" ),
            @ApiImplicitParam( name="sellOrderLine", value = "销售订单行，支持模糊查询。" ),
            @ApiImplicitParam( name="source", value = "来源，MES=mes自建" )
    })
    public ResponseData<Page<ProductionOrder>> getPage(@RequestParam Map<String, Object> params) {
        params = StringUtils.replaceMatchTextByMap(params);
        return ResponseData.success(productionOrderService.queryPage(params));
    }

    /**
     * 查询列表LOV
     * @param params 分页查询参数
     * */
    @PostMapping("/lov/page")
    @ApiOperation(value = "分页查询生产订单列表-LOV")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="orderNo", value = "订单编号，支持模糊查询。" ),
            @ApiImplicitParam( name="itemNo", value = "物料编号，支持模糊查询。" ),
            @ApiImplicitParam( name="itemName", value = "物料名称，支持模糊查询。" ),
            @ApiImplicitParam( name="status", value = "订单状态,字典:PRO_ORD_STATUS" ),
            @ApiImplicitParam( name="type", value = "订单类型,字典:PRO_ORD_TYPE" ),
            @ApiImplicitParam( name="sellOrderNo", value = "销售订单编号，支持模糊查询。" ),
            @ApiImplicitParam( name="sellOrderLine", value = "销售订单行，支持模糊查询。" ),
            @ApiImplicitParam( name="source", value = "来源，MES=mes自建" )
    })
    public ResponseData<Page<ProductionOrder>> getPageLov(@RequestBody Map<String, Object> params) {
        params = StringUtils.replaceMatchTextByMap(params);
        return ResponseData.success(productionOrderService.queryPage(params));
    }

    /**
     * 详情
     * @param id 订单id
     * */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "根据id查询生产订单详情")
    public ResponseData<ProductionOrder> getDetailById(@PathVariable("id") String id) {
        return ResponseData.success(productionOrderService.detailById(id));
    }

    /**
     * 删除
     * */
    @DeleteMapping("/delete/{id}")
    public ResponseData<Boolean> deleteById(@PathVariable("id") String id) {
        return ResponseData.success(productionOrderService.deleteById(id));
    }



}
