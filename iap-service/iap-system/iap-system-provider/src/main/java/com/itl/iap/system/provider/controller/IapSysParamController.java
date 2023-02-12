package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.entity.IapSysParam;
import com.itl.iap.system.provider.service.impl.IapSysParamServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/iapSysParam")
public class IapSysParamController extends BaseController {

    @Autowired
    private IapSysParamServiceImpl iapSysParamService;


    @ApiOperation(value = "list", notes = "参数组列表", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData getUserRoleIds(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "name", required = false) String name, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(iapSysParamService.findList(type,name,page,pageSize));
    }

    @ApiOperation(value = "listByState", notes = "参数组列表", httpMethod = "POST")
    @PostMapping(value = "/listByState")
    public ResponseData getUserRoleIdsByState(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "name", required = false) String name, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(iapSysParamService.findListByState(type,name,page,pageSize));

    }

    @ApiOperation(value = "queryForLov", notes = "lov查询", httpMethod = "POST")
    @PostMapping(value = "/queryForLov")
    public ResponseData queryForLov(@RequestBody Map<String, Object> params) {
        return ResponseData.success(iapSysParamService.queryForLov(params));
    }

    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody IapSysParam iapSysParam) {
        iapSysParamService.save(iapSysParam);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(iapSysParamService.findById(id));
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        iapSysParamService.delete(ids);
        return ResponseData.success(true);
    }
}
