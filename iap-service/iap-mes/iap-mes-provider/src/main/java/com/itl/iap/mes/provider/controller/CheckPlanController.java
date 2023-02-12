package com.itl.iap.mes.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.CheckPlanDto;
import com.itl.iap.mes.api.dto.CheckPlanEnableDto;
import com.itl.iap.mes.api.dto.CheckPlanQueryDto;
import com.itl.iap.mes.api.dto.UpkeepPlanEnableDto;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.provider.service.impl.CheckPlanServiceImpl;
import com.itl.iap.notice.api.dto.SendMsgDTO;
import com.itl.iap.notice.client.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 点检计划维护
 */
@RestController
@RequestMapping("/m/repair/check")
@Api(tags = "点检计划-维护")
@Validated
@RequiredArgsConstructor
public class CheckPlanController {

    final private CheckPlanServiceImpl checkPlanService;


    @ApiOperation(value = "新增设备点检计划", notes = "新增设备点检计划", httpMethod = "PUT")
    @ApiImplicitParam(name = "type", value = "0 保存 1 保存加执行", required = true, paramType = "Integer")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody CheckPlanDto checkPlanDto, @RequestParam Integer type) throws CommonException {
        checkPlanService.save(checkPlanDto, type);
        return ResponseData.success(true);
    }


    @ApiOperation(value = "点检计划维护列表", notes = "点检计划维护列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, paramType = "Integer")
    })
    @PostMapping(value = "/list")
    public ResponseData<IPage<CheckPlan>> queryPage(@RequestBody @Validated CheckPlanQueryDto checkPlanQueryDto, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(checkPlanService.findList(checkPlanQueryDto, page, pageSize));
    }

    @ApiOperation(value = "listByState", notes = "根据分组id查询字典ByState", httpMethod = "POST")
    @PostMapping(value = "/listByState")
    public ResponseData queryPageByState(@RequestBody CheckPlan checkPlan, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(checkPlanService.findListByState(checkPlan, page, pageSize));
    }


    @ApiOperation(value = "查看设备点检计划", notes = "查看设备点检计划")
    @ApiImplicitParam(name = "id", value = "点检计划id", required = true, paramType = "String")
    @GetMapping(value = "/getById/{id}")
    public ResponseData<CheckPlan> getById(@PathVariable String id) {
        return ResponseData.success(checkPlanService.findById(id));
    }

    @ApiOperation(value = "删除设备点检计划", notes = "删除设备点检计划")
    @ApiImplicitParam(name = "id", value = "点检计划id", required = true, paramType = "String")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        checkPlanService.delete(ids);
        return ResponseData.success(true);
    }


    @ApiOperation(value = "启用/暂停计划", notes = "启用/暂停计划")
    @PostMapping(value = "/enable")
    public ResponseData enable(@RequestBody @Validated CheckPlanEnableDto dto) {
        checkPlanService.enable(dto);
        return ResponseData.success(true);
    }

}
