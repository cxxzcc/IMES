package com.itl.iap.mes.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.UpkeepPlanDto;
import com.itl.iap.mes.api.dto.UpkeepPlanEnableDto;
import com.itl.iap.mes.api.dto.UpkeepPlanQueryDto;
import com.itl.iap.mes.api.dto.UpkeepPlanQueryDto1;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import com.itl.iap.mes.provider.service.impl.UpkeepPlanServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/m/repair/upkeep")
@Api(tags = "保养计划-维护")
@Validated
public class UpkeepPlanController extends BaseController {

    @Autowired
    private UpkeepPlanServiceImpl upkeepPlanService;

    @ApiOperation(value = "保养计划维护列表", notes = "保养计划维护列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, paramType = "Integer")
    })
    @PostMapping(value = "/list")
    public ResponseData<IPage<UpkeepPlan>> queryPage(@RequestBody UpkeepPlanQueryDto upkeepPlanQueryDto,
                                                     @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(upkeepPlanService.findList(upkeepPlanQueryDto, page, pageSize));
    }

    @ApiOperation(value = "listByState", notes = "根据分组id查询字典", httpMethod = "POST")
    @PostMapping(value = "/listByState")
    public ResponseData queryPageByState(@RequestBody UpkeepPlan upkeepPlan, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(upkeepPlanService.findListByState(upkeepPlan, page, pageSize));
    }

    @ApiOperation(value = "保存保养计划维护", notes = "保存保养计划维护")
    @ApiImplicitParam(name = "type", value = "0 保存 1 保存加执行", required = true, paramType = "Integer")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody UpkeepPlanDto upkeepPlanDto, @RequestParam Integer type) throws CommonException {
        upkeepPlanService.save(upkeepPlanDto, type);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "查询保养计划维护", notes = "查询保养计划维护")
    @ApiImplicitParam(name = "id", value = "点检计划id", required = true, paramType = "String")
    @GetMapping(value = "/getById/{id}")
    public ResponseData<UpkeepPlan> getById(@PathVariable String id) {
        return ResponseData.success(upkeepPlanService.findById(id));
    }

    @ApiOperation(value = "删除保养计划维护", notes = "删除保养计划维护")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        upkeepPlanService.delete(ids);
        return ResponseData.success(true);
    }


    @ApiOperation(value = "启用/暂停计划", notes = "启用/暂停计划")
    @PostMapping(value = "/enable")
    public ResponseData enable(@RequestBody @Validated UpkeepPlanEnableDto dto) {
        upkeepPlanService.enable(dto);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "保养计划维护列表1", notes = "保养计划维护列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, paramType = "Integer")
    })
    @PostMapping(value = "/list1")
    public ResponseData<IPage<UpkeepPlan>> queryPage1(@RequestBody UpkeepPlanQueryDto1 upkeepPlanQueryDto,
                                                     @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(upkeepPlanService.findList1(upkeepPlanQueryDto, page, pageSize));
    }

}
