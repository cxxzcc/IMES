package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ShopOrderPackRuleQueryDto;
import com.itl.mes.core.api.service.ShopOrderPackRuleService;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import com.itl.mes.core.api.vo.ShopOrderPackRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/6/2
 */
@RestController
@RequestMapping("/shopOrderPackRule")
@Api(tags = " 工单包装规则控制层" )
public class ShopOrderPackRuleController {

    @Autowired
    ShopOrderPackRuleService shopOrderPackRuleService;

    @PostMapping("findList")
    @ApiOperation(value="查询工单包装规则集合")
    public ResponseData<ShopOrderPackRuleVo> findList(@RequestBody ShopOrderPackRuleQueryDto shopOrderPackRuleQueryDto){

        return ResponseData.success(shopOrderPackRuleService.findList(shopOrderPackRuleQueryDto));

    }



    @DeleteMapping("deleteByIds")
    @ApiOperation(value="批量删除")
    public ResponseData deleteByIds(@RequestBody List<String> bos){
        shopOrderPackRuleService.removeByIds(bos);
        return ResponseData.success();
    }

    @GetMapping("/listPackRuleDetailByShopOrderBo")
    @ApiOperation(value = "listPackRuleDetailByShopOrderBo for feign")
    public List<ShopOrderPackRuleDetailVo> listPackRuleDetailByShopOrderBo(@RequestParam("shopOrderBo") String shopOrderBo) {
        return shopOrderPackRuleService.listPackRuleDetailByShopOrderBo(shopOrderBo);
    }

}
