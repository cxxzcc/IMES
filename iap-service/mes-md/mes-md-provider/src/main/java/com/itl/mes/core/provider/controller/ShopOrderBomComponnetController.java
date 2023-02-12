package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetQueryDto;
import com.itl.mes.core.api.service.ShopOrderBomComponnetService;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/5/27
 */
@RestController
@RequestMapping("/shopOrderBomComponnet")
@Api(tags = " 工单BOM组件控制层" )
public class ShopOrderBomComponnetController {

    @Autowired
    ShopOrderBomComponnetService shopOrderBomComponnetService;

    @PostMapping("findList")
    @ApiOperation(value="查询工单的工单/工序BOM组件集合")
    public ResponseData<IPage<ShopOrderBomComponnetVo>> findList(@RequestBody ShopOrderBomComponnetQueryDto shopOrderBomComponnetQueryDto){

        return ResponseData.success(shopOrderBomComponnetService.findList(shopOrderBomComponnetQueryDto));

    }

    @GetMapping("queryBomByShopOrderBo")
    @ApiOperation(value="queryBomByShopOrderBo for feign")
    public List<ShopOrderBomComponnetVo> queryBomByShopOrderBo(@RequestParam("shopOrderBo") String shopOrderBo,
                                                               @RequestParam("type") String type) {
        return shopOrderBomComponnetService.queryBomByShopOrderBo(shopOrderBo, type);
    }



    @DeleteMapping("deleteByIds")
    @ApiOperation(value="批量删除")
    public ResponseData deleteByIds(@RequestBody List<String> bos){
        shopOrderBomComponnetService.removeByIds(bos);
        return ResponseData.success();
    }


}
