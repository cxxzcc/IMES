package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.UpdateBatchSnProcessDto;
import com.itl.mes.me.api.dto.UpdateSingleSnProcessDto;
import com.itl.mes.me.api.dto.UpdateSnRouteDto;
import com.itl.mes.me.api.entity.MeSnRouter;
import com.itl.mes.me.api.service.MeSnRouterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 条码工艺路线
 * @author dengou
 * @date 2021/11/26
 */
@RestController
@RequestMapping("/snRoute")
public class SnRouteController {

    @Autowired
    private MeSnRouterService meSnRouterService;

    /**
     * 查询工艺路线
     * */
    @ApiOperation(value = "获取条码对应的工艺路线")
    @PostMapping("/route")
    public ResponseData getSnRouteBySnList(@RequestBody List<String> snList) {
        return ResponseData.success(meSnRouterService.getSnRouteBySnList(snList));
    }

    /**
     * 更新条码工艺路线 me_sn_route
     * */
    @ApiOperation(value = "更新条码工艺路线")
    @PutMapping("/save")
    public ResponseData saveSnRoute(@RequestBody UpdateSnRouteDto updateSnRouteDto) {
        return ResponseData.success(meSnRouterService.saveSnRoute(updateSnRouteDto));
    }

    /**
     * feign接口 条码转移保存条码工艺路线 me_sn_route
     * */
    @ApiOperation(value = "新增条码工艺路线")
    @PostMapping("/add")
    public ResponseData addSnRoute(@RequestBody UpdateSnRouteDto updateSnRouteDto) {
        return ResponseData.success(meSnRouterService.addSnRoute(updateSnRouteDto));
    }

    /**
     * feign接口 根据条码查询工艺路线 me_sn_route
     * */
    @ApiOperation(value = "根据条码查询工艺路线")
    @GetMapping("/getBySn")
    public ResponseData<MeSnRouter> getBySn(@RequestParam("sn") String sn, @RequestParam("site") String site) {
        return ResponseData.success(meSnRouterService.getBySn(sn, site));
    }

    /**
     * 更新工序-获取可选择的下工序列表
     * @param sn sn
     * */
    @ApiOperation(value = "获取可选择的下工序列表")
    @GetMapping("/nextProcessList")
    public ResponseData<List<Map<String, Object>>> getNexProcessListBySn(String sn) {
        return ResponseData.success(meSnRouterService.getNexProcessListBySn(sn));
    }

    /**
     * 调整工序
     * @param updateSnProcessDto 更新参数
     * */
    @ApiOperation(value = "单个调整工序")
    @PutMapping("/nextProcess")
    public ResponseData<Boolean> updateNexProcessBySn(@RequestBody UpdateSingleSnProcessDto updateSnProcessDto) {
        return ResponseData.success(meSnRouterService.updateNexProcessBySn(updateSnProcessDto.getSn(), updateSnProcessDto.getProcessId()));
    }

    /**
     * 批量调整工序
     * */
    @ApiOperation(value = "批量调整工序")
    @PutMapping("/batch/nextProcess")
    public ResponseData<Boolean> updateNexProcessBySnBatch(@RequestBody UpdateBatchSnProcessDto updateBatchSnProcessDto) {
        return ResponseData.success(meSnRouterService.updateNexProcessBySnBatch(updateBatchSnProcessDto));
    }



}
