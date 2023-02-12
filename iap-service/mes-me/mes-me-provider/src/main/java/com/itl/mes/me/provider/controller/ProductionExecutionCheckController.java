package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.service.ProductionExecutionCheckService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产执行判断controller
 * @author dengou
 * @date 2021/12/14
 */
@RestController
@RequestMapping("/product/execution/check")
public class ProductionExecutionCheckController {

    @Autowired
    private ProductionExecutionCheckService productionExecutionCheckService;

    /**
     * 生产执行前置判断
     * 1. 判断当前登录用户是否拥有当前工位工序资质证明
     * */
    @GetMapping("/pre")
    @ApiOperation(value = "生产执行前置判断")
    public ResponseData preCheck() {
        return ResponseData.success(productionExecutionCheckService.preCheck());
    }

}
