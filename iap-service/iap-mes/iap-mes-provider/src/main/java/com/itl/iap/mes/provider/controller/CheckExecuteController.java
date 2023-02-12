package com.itl.iap.mes.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.CheckExecuteQueryDto;
import com.itl.iap.mes.api.dto.RepairExecuteCountStatisticsDTO;
import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.mes.provider.service.impl.CheckExecuteServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/m/repair/execute")
@Api(tags = "点检计划-执行")
public class CheckExecuteController extends BaseController {

    @Autowired
    private CheckExecuteServiceImpl checkExecuteService;

    @ApiOperation(value = "点检计划执行工单列表", notes = "点检计划执行工单列表")
    @PostMapping(value = "/list")
    public ResponseData<IPage<CheckExecute>> queryPage(@RequestBody CheckExecuteQueryDto checkExecuteQueryDto, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(checkExecuteService.findList(checkExecuteQueryDto,page,pageSize));
    }

    @ApiOperation(value = "查看设备点检计划工单", notes = "查看设备点检计划工单")
    @ApiImplicitParam(name = "id", value = "点检计划工单id", required = true, paramType = "String")
    @GetMapping(value = "/getById/{id}")
    public ResponseData<CheckExecute> getById(@PathVariable String id) throws CommonException {
        return ResponseData.success(checkExecuteService.findById(id));
    }

    @ApiOperation(value = "新增设备点检计划工单", notes = "新增设备点检计划工单")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody CheckExecute checkExecute) {
        checkExecuteService.save(checkExecute);
        return ResponseData.success(true);
    }

    /**
     * 点检工单统计
     * @param params 查询参数
     * */
    @GetMapping("/statistics/executeCount")
    @ApiOperation(value = "点检工单统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间, yyyy-MM-dd HH:mm:ss", required = true, paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间, yyyy-MM-dd HH:mm:ss", required = true, paramType = "String")
    })
    public ResponseData<List<RepairExecuteCountStatisticsDTO>> checkExecuteStatistics(@RequestParam Map<String, Object> params) {
        params.put("site", UserUtils.getSite());
        return ResponseData.success(checkExecuteService.checkExecuteStatistics(params));
    }

}