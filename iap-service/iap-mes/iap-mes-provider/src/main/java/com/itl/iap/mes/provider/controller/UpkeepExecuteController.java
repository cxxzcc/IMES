package com.itl.iap.mes.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.RepairExecuteCountStatisticsDTO;
import com.itl.iap.mes.api.dto.UpkeepExecuteQueryDto;
import com.itl.iap.mes.api.entity.UpkeepExecute;
import com.itl.iap.mes.provider.service.impl.UpkeepExecuteServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/m/repair/upkeep/execute")
@Api(tags = "保养计划-执行")
public class UpkeepExecuteController extends BaseController {

    @Autowired
    private UpkeepExecuteServiceImpl upkeepExecuteService;


    @ApiOperation(value = "保养计划执行列表", notes = "保养计划执行列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, paramType = "Integer")
    })
    @PostMapping(value = "/list")
    public ResponseData<IPage<UpkeepExecute>> queryPage(@RequestBody UpkeepExecuteQueryDto upkeepExecuteQueryDto, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {

        return ResponseData.success(upkeepExecuteService.findList(upkeepExecuteQueryDto, page, pageSize));
    }

    @ApiOperation(value = "查询保养计划执行", notes = "查询保养计划执行")
    @ApiImplicitParam(name = "id", value = "保养计划id", required = true, paramType = "String")
    @GetMapping(value = "/getById/{id}")
    public ResponseData<UpkeepExecute> getById(@PathVariable String id) {
        return ResponseData.success(upkeepExecuteService.findById(id));
    }

    @ApiOperation(value = "保养计划执行保存", notes = "保养计划执行保存")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody UpkeepExecute upkeepExecute) throws CommonException {
        upkeepExecuteService.save(upkeepExecute);
        return ResponseData.success(true);
    }


    /**
     * 点检工单统计
     * @param params 查询参数
     * */
    @GetMapping("/statistics/executeCount")
    @ApiOperation(value = "保养工单统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间, yyyy-MM-dd HH:mm:ss", required = true, paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间, yyyy-MM-dd HH:mm:ss", required = true, paramType = "String")
    })
    public ResponseData<List<RepairExecuteCountStatisticsDTO>> checkExecuteStatistics(@RequestParam Map<String, Object> params) {
        params.put("site", UserUtils.getSite());
        return ResponseData.success(upkeepExecuteService.checkExecuteStatistics(params));
    }

}
