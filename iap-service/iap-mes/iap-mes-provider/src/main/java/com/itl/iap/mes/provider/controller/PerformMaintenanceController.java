package com.itl.iap.mes.provider.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.CorrectiveMaintenance;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.service.impl.CorrectiveMaintenanceServiceImpl;
import com.itl.iap.mes.provider.service.impl.PerformMaintenanceServiceImpl;
import com.itl.mes.core.api.dto.AbnormalCountStatisticsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/m/repair/performMaintenance")
@Api(tags = "维修执行")
public class PerformMaintenanceController {

    @Autowired
    private PerformMaintenanceServiceImpl performMaintenanceService;
    @Autowired
    private CorrectiveMaintenanceMapper correctiveMaintenanceMapper;
    @Autowired
    private CorrectiveMaintenanceServiceImpl correctiveMaintenanceService;

    @ApiOperation(value = "getWorkOrderById", notes = "查询待维修工单", httpMethod = "GET")
    @GetMapping(value = "/getWorkOrderById/{id}")
    public ResponseData<CorrectiveMaintenance> getWorkOrderById(@PathVariable String id) {
        return ResponseData.success(performMaintenanceService.getWorkOrderById(id));
    }

    @ApiOperation(value = "getWorkOrderByAndon", notes = "查询待维修工单", httpMethod = "GET")
    @GetMapping(value = "/getWorkOrderByAndon/{andonBo}")
    public ResponseData<CorrectiveMaintenance> getWorkOrderByandonBo(@PathVariable String andonBo) {

        final List<CorrectiveMaintenance> correctiveMaintenances = correctiveMaintenanceMapper.selectList(new QueryWrapper<CorrectiveMaintenance>().lambda().eq(CorrectiveMaintenance::getAndonBo, andonBo).orderByDesc(CorrectiveMaintenance::getCreateTime));
        if (CollUtil.isNotEmpty(correctiveMaintenances)) {
            final String id = correctiveMaintenances.get(0).getId();
            return ResponseData.success(correctiveMaintenanceService.getById(id));
        }
        return ResponseData.success();
    }

    /**
     * 设备维修 受理或完成提交
     */
    @ApiOperation(value = "受理/提交", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody CorrectiveMaintenance correctiveMaintenance) throws CommonException {
        performMaintenanceService.save(correctiveMaintenance);
        return ResponseData.success(true);
    }


    @ApiOperation(value = "assign", notes = "维修工单指派")
    @PostMapping("/assign/{id}")
    public ResponseData<Boolean> assign(@RequestBody @NotEmpty(message = "用户id列表不能为空") List<String> userIds,
                                        @PathVariable("id") @NotNull(message = "工单id不能为空") String id) {
        return ResponseData.success(performMaintenanceService.assign(userIds, id));
    }


    @ApiOperation(value = "undoRepairById", notes = "撤销维修", httpMethod = "GET")
    @PutMapping(value = "/undoRepairById/{id}")
    public ResponseData<CorrectiveMaintenance> undoRepairById(@PathVariable String id) {
        return ResponseData.success(performMaintenanceService.undoRepairById(id));
    }

    /**
     * 统计各个状态维修工单的的数量
     */
    @ApiOperation(value = "统计各个状态维修工单的数量")
    @GetMapping("/statistics/countGroupByState")
    public ResponseData<List<AbnormalCountStatisticsDTO>> queryRepairCountStatisticsByState() {
        return ResponseData.success(performMaintenanceService.queryRepairCountStatisticsByState(UserUtils.getSite()));
    }



    /*
    @ApiOperation(value = "mylist", notes = "我的设备维修列表", httpMethod = "POST")
    @PostMapping(value = "/mylist")
    public ResponseData get(@RequestBody CorrectiveMaintenance correctiveMaintenance, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(performMaintenanceService.findListForPlanRepairUser(correctiveMaintenance, page, pageSize));
    }
*/
}
