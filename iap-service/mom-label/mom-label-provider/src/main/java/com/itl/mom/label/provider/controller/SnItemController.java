package com.itl.mom.label.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.entity.label.SnItem;
import com.itl.mom.label.api.service.SnItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物料标签管理表
 *
 * @author GKL
 * @date 2021/11/10 - 15:40
 * @since 2021/11/10 - 15:40 星期三 by GKL
 */
@RestController
@RequestMapping("/momSnItem")
@Api(tags = " 物料标签管理表")
@Slf4j
@RequiredArgsConstructor
public class SnItemController {

    private final SnItemService snItemService;

    @GetMapping("/querySnItemByItemBoAndSnBo")
    @ApiOperation(value = "查询物料标签管理")
    ResponseData<SnItem> querySnItemByItemBoAndSnBo(@RequestParam("itemBo") String itemBo, @RequestParam("snBo") String snBo) {
        try {
            return snItemService.querySnItemByItemBoAndSnBo(itemBo, snBo);

        } catch (Exception e) {
            log.info("Exception is ->{}", e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

    @GetMapping("/updateSysl")
    @ApiOperation(value = "通过snbo修改剩余数量")
    ResponseData<Boolean> updateSysl(@RequestParam("id")String id, @RequestParam("sysl") BigDecimal sysl){
        try {
            return snItemService.updateSysl(id,sysl);
        }catch (Exception e){
            return  ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/updateSyslTpZero")
    @ApiOperation(value = "通过列表修改对应的剩余数量为0")
    ResponseData<Boolean> updateSyslByBoList(@RequestBody List<String> boList){
        try {
            return snItemService.updateSyslByBoList(boList);
        }catch (Exception e){
            return  ResponseData.error(e.getMessage());
        }
    }

}
