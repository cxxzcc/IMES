package com.itl.mes.core.client.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.MeProductInspectionItemsOrderServiceImpl;
import com.itl.mes.me.api.dto.MeProductGroupInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsOrderDto;
import com.itl.mes.me.api.entity.MeProductGroupInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


/**
 * 产品检验项目-工单副本
 *
 * @author chenjx1
 * @date 2021-10-20
 */
@FeignClient(value = "mes-me-provider",contextId = "MeProductInspectionItemsOrder",
        fallback = MeProductInspectionItemsOrderServiceImpl.class,
        configuration = FallBackConfig.class)
@Api(tags = "产品检验项目-工单副本")
public interface MeProductInspectionItemsOrderService {

    /**
     * 列表-工单副本
     */
    @PostMapping("/productOrder/listItemsOrder")
    ResponseData<IPage<MeProductInspectionItemsOrderEntity>> listItemsOrder(@RequestBody MeProductInspectionItemsOrderDto meProductInspectionItemsOrderDto);

    /**
     * 列表-产品检验
     */
    @PostMapping("/productOrder/listItems")
    List<MeProductInspectionItemsEntity> listItems(@RequestBody MeProductInspectionItemsDto meProductInspectionItemsDto);

    /**
     * 列表-产品检验组
     */
    @PostMapping("/productOrder/listGroupItems")
    List<MeProductGroupInspectionItemsEntity> listGroupItems(@RequestBody MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto);


    /**
     * 信息
     */
    @GetMapping("/productOrder/info/{id}")
    ResponseData info(@PathVariable("id") Integer id);

    /**
     * 列表
     */
    @PostMapping("/productOrder/listByIds")
    ResponseData<Collection<MeProductInspectionItemsOrderEntity>> listByIds(@RequestBody List<Integer> ids);

    /**
     * 保存
     */
    @PostMapping("/productOrder/save")
    ResponseData save(@RequestBody MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity) throws CommonException;

    /**
     * 集合保存
     */
    @PostMapping("/productOrder/saveList")
    ResponseData saveList(@RequestBody List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList) throws CommonException;

    /**
     * 修改
     */
    @PostMapping("/productOrder/update")
    ResponseData update(@RequestBody MeProductInspectionItemsOrderEntity meProductInspectionItems);

    /**
     * 删除
     */
    @PostMapping("/productOrder/delete")
    ResponseData delete(@RequestBody Integer[] ids);

    /**
     * 删除检验项目副本
     */
    @PostMapping("/productOrder/deleteOrderItems")
    ResponseData deleteOrderItems(@RequestParam String orderBo);

}
