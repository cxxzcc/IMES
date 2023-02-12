package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.RepairOperateLog;
import com.itl.iap.mes.api.service.RepairOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工单操作记录
 * @author dengou
 * @date 2021/10/26
 */
@RestController
@RequestMapping("/repair/operate/log")
@Api(tags = "工单操作记录" )
public class RepairOperateLogController {


    @Autowired
    private RepairOperateLogService repairOperateLogService;

    @GetMapping("/list")
    @ApiOperation(value = "工单操作记录列表", notes = "工单操作记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "工单id", required = true, paramType = "String"),
            @ApiImplicitParam(name = "orderType", value = "工单类型, corrective=维修;upkeep=保养;check=点检", required = true, paramType = "String")
    })
    public ResponseData<List<RepairOperateLog>> getListByRepairId(String orderId, String orderType) {
        return ResponseData.success(repairOperateLogService.getListByOrderId(orderId, orderType, UserUtils.getSite()));
    }
}
