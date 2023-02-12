package com.itl.iap.system.provider.controller;


import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.entity.IapSysParamType;
import com.itl.iap.system.provider.service.impl.IapSysParamTypeServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("iapSysParamType")
public class IapSysParamTypeController extends BaseController {

    @Autowired
    private IapSysParamTypeServiceImpl iapSysParamTypeService;



    @ApiOperation(value = "list", notes = "根据分组id查询字典", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData list(@RequestBody IapSysParamType iapSysParamType, @RequestParam Integer page, @RequestParam Integer pageSize) {
       return ResponseData.success(iapSysParamTypeService.findList(iapSysParamType,page,pageSize));
    }

    @ApiOperation(value = "listByState", notes = "根据分组id查询字典", httpMethod = "POST")
    @PostMapping(value = "/listByState")
    public ResponseData listByState(@RequestBody IapSysParamType iapSysParamType, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(iapSysParamTypeService.findListByState(iapSysParamType,page,pageSize));
    }

    @ApiOperation(value = "queryForLov", notes = "lov查询", httpMethod = "POST")
    @PostMapping(value = "/queryForLov")
    public ResponseData queryForLov(@RequestBody Map<String, Object> params) {
        return ResponseData.success(iapSysParamTypeService.queryForLov(params));
    }

    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody IapSysParamType iapSysParamType) {
        iapSysParamTypeService.save(iapSysParamType);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) throws CommonException {
        iapSysParamTypeService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(iapSysParamTypeService.findById(id));

    }

}
