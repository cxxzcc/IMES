package com.itl.mes.core.client.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.MeProductInspectionItemsOrderNcCodeServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


/**
 * 产品检验项目不合格代码表-工单副本
 *
 * @author chenjx1
 * @date 2021-12-15
 */
@FeignClient(value = "mes-md-provider",contextId = "MeProductInspectionItemsOrderNcCode",
        fallback = MeProductInspectionItemsOrderNcCodeServiceImpl.class,
        configuration = FallBackConfig.class)
@Api(tags = "产品检验项目不合格代码表-工单副本")
public interface MeProductInspectionItemsOrderNcCodeService {

    /**
     * 列表-产品检验项不良代码
     */
    @PostMapping("/productOrderNcCodes/listItemNcCodes")
    ResponseData<List<MeProductInspectionItemsOrderNcCode>> listItemNcCodes(@RequestBody MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode);

    /**
     * 根据BO查询
     */
    @GetMapping("/productOrderNcCodes/info/{bo}")
    ResponseData info(@PathVariable("bo") String bo);

    /**
     * 根据bos查询列表
     */
    @PostMapping("/productOrderNcCodes/listByBos")
    ResponseData<Collection<MeProductInspectionItemsOrderNcCode>> listByBos(@RequestBody List<String> bos);

    /**
     * 保存
     */
    @PostMapping("/productOrderNcCodes/save")
    ResponseData save(@RequestBody MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException;

    /**
     * 保存集合
     */
    @PostMapping("/productOrderNcCodes/saveList")
    ResponseData saveList(@RequestBody List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList) throws CommonException;

    /**
     * 修改
     */
    @PostMapping("/productOrderNcCodes/update")
    ResponseData update(@RequestBody MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException;

    /**
     * 根据BO删除
     */
    @PostMapping("/productOrderNcCodes/deleteByBos")
    ResponseData deleteByBos(@RequestBody String[] bos);

    /**
     * 根据工单Bo删除不良代码
     */
    @PostMapping("/productOrderNcCodes/deleteNcCodesByOrderBo")
    ResponseData deleteNcCodesByOrderBo(@RequestParam String orderBo) throws CommonException;


    /**
     * 根据工单BO+产品检验项目ID删除不良代码
     */
    @PostMapping("/productOrderNcCodes/deleteByInspectionItemId")
    ResponseData deleteByInspectionItemId(@RequestParam String orderBo, @RequestParam Integer inspectionItemId) throws CommonException;


    /**
     * 根据工单BO+产品检验项目ID+产品类型删除不良代码
     */
    @PostMapping("/productOrderNcCodes/deleteByInspectionItemIdItemType")
    ResponseData deleteByInspectionItemIdItemType(@RequestParam String orderBo, @RequestParam Integer inspectionItemId, @RequestParam String itemType) throws CommonException;

}
