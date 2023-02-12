package com.itl.iap.mes.provider.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.CheckRecordDTO;
import com.itl.iap.mes.api.dto.RepairRecordDTO;
import com.itl.iap.mes.api.dto.SparePartChangeRecordDTO;
import com.itl.iap.mes.api.dto.UpkeepRecordDTO;
import com.itl.iap.mes.api.service.SparePartStorageRecordService;
import com.itl.iap.mes.provider.service.impl.CheckExecuteServiceImpl;
import com.itl.iap.mes.provider.service.impl.CorrectiveMaintenanceServiceImpl;
import com.itl.iap.mes.provider.service.impl.UpkeepExecuteServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 设备履历
 * @author dengou
 * @date 2021/9/27
 */
@RestController
@RequestMapping("/m/device/record")
@Api(tags = " 设备履历" )
public class DeviceRecordController {


    @Autowired
    private CorrectiveMaintenanceServiceImpl correctiveMaintenanceService;
    @Autowired
    private UpkeepExecuteServiceImpl upkeepExecuteService;
    @Autowired
    private CheckExecuteServiceImpl checkExecuteService;
    @Autowired
    private SparePartStorageRecordService sparePartStorageRecordService;

    /**
     * 设备维修记录
     * @param params code=设备编号；分页参数
     * @return 维修记录列表
     * */
    @GetMapping("/repair")
    @ApiOperation(value="设备维修记录")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="code", value = "设备编号" )
    })
    public ResponseData<Page<RepairRecordDTO>> repairRecord(@RequestParam Map<String, Object> params) {
        return ResponseData.success(correctiveMaintenanceService.repairRecord(params));
    }


    /**
     * 设备保养记录
     * @param params code=设备编号；分页参数
     * @return 保养记录列表
     * */
    @GetMapping("/upkeep")
    @ApiOperation(value="设备保养记录")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="code", value = "设备编号" )
    })
    public ResponseData<Page<UpkeepRecordDTO>> upkeepRecord(@RequestParam Map<String, Object> params) {
        return ResponseData.success(upkeepExecuteService.upkeepRecord(params));
    }

    /**
     * 点检保养记录
     * @param params code=设备编号；分页参数
     * @return 点检记录列表
     * */
    @GetMapping("/check")
    @ApiOperation(value="点检保养记录")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="code", value = "设备编号" )
    })
    public ResponseData<Page<CheckRecordDTO>> checkRecord(@RequestParam Map<String, Object> params) {
        return ResponseData.success(checkExecuteService.checkRecord(params));
    }


    /**
     * 备件更换记录
     * @param params code=设备编号；分页参数
     * @return 备件更换记录列表
     * */
    @GetMapping("/sparePart")
    @ApiOperation(value="备件更换记录")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="code", value = "设备编号" )
    })
    public ResponseData<Page<SparePartChangeRecordDTO>> sparePartChangeRecord(@RequestParam Map<String, Object> params) {
        return ResponseData.success(sparePartStorageRecordService.sparePartChangeRecord(params));
    }


}
