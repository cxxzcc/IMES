package com.itl.iap.mes.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.CorrectiveMaintenance;
import com.itl.iap.mes.provider.service.impl.CorrectiveMaintenanceServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/m/repair/maintenance")
@Api(tags = "新增维修工单" )
public class CorrectiveMaintenanceController extends BaseController {

    @Autowired
    private CorrectiveMaintenanceServiceImpl correctiveMaintenanceService;

    @ApiOperation(value = "getById", notes = "查询单条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(correctiveMaintenanceService.getById(id));
    }

    @ApiOperation(value = "list", notes = "设备维修列表", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData <IPage<CorrectiveMaintenance>> getUserRoleIds(@RequestBody CorrectiveMaintenance correctiveMaintenance, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(correctiveMaintenanceService.findList(correctiveMaintenance,page,pageSize));
    }

    /**
     * 设备维修 插入
     */
    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody CorrectiveMaintenance correctiveMaintenance) throws CommonException {
        correctiveMaintenanceService.save(correctiveMaintenance);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        correctiveMaintenanceService.delete(ids);
        return ResponseData.success(true);
    }
}
