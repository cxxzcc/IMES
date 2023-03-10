package com.itl.mes.me.provider.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.MeSfcHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.entity.MeSfcNcLog;
import com.itl.mes.me.api.service.*;
import com.itl.mes.me.api.vo.DetectionSnInfoVo;
import com.itl.mes.me.api.vo.RepairSnInfoVo;
import com.itl.mes.me.provider.mapper.MeSfcMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author 崔翀赫
 * @date 2021/1/26$
 * @since JDK1.8
 */
@RestController
@Api(tags = "车间作业控制信息表")
@RequestMapping("meSfc")
public class MeSfcController {

    @Resource
    private MeSfcService meSfcService;

    @Resource
    private SnService snService;
    @Resource
    private MeSfcWipLogService meSfcWipLogService;

    @Autowired
    private MeSfcMapper meSfcMapper;



    @Autowired
    private MeSfcNcLogService meSfcNcLogService;

    /**
     * 过站采集
     */
    @GetMapping("/Acquisition")
    @ApiOperation(value = "过站采集")
    public ResponseData acquisition(@RequestParam String sn) {
        MeSfc byId = meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo());

        if (byId == null) {
            return ResponseData.error("该标签未维护!");
        }
        if ("已采集".equals(byId.getState())) {
            return ResponseData.error("该标签已采集!");
        }
        return ResponseData.success(meSfcService.acquisition(byId));
    }


    /**
     * 根据条码获取物料
     */
    @GetMapping("/getItem")
    @ApiOperation(value = "根据条码获取物料")
    public ResponseData getItem(@RequestParam String sn) {
        return ResponseData.success(snService.getItem(sn));
    }


    @GetMapping("/getMeSfcSchedule")
    @ApiOperation(value = "根据条码获取排程")
    public ResponseData getMeSfcSchedule(@RequestParam String sn) {
        return ResponseData.success(meSfcService.getMeSfcSchedule(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo()));
    }

    @GetMapping("/getBasicInformation")
    @ApiOperation(value = "SN-基本信息")
    public ResponseData getBasicInformation(@RequestParam String sn) throws CommonException {
        MeSfc byId = meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo());

        if (byId == null) {
            return new ResponseData(ResultResponseEnum.SUCCESS.getCode(), "该标签未维护!", null);
        }
        return ResponseData.success(meSfcService.getBasicInformation(sn));
    }

    @GetMapping("/getKeyPartsBarcode")
    @ApiOperation(value = "SN-关键件条码")
    public ResponseData getKeyPartsBarcode(@RequestParam String sn) {
        return ResponseData.success(meSfcService.getKeyPartsBarcode(sn));
    }

    @GetMapping("/getBomInfo")
    @ApiOperation("SN-BOM信息")
    public ResponseData getBomInfo(@RequestParam("sn") String sn) {
        return ResponseData.success(meSfcService.getBomInfo(sn));
    }

    @GetMapping("/getStationRecord")
    @ApiOperation(value = "SN-过站记录")
    public ResponseData getStationRecord(@RequestParam String sn) throws CommonException {
        MeSfc byId = meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo());
        if (byId == null) {
            return new ResponseData(ResultResponseEnum.SUCCESS.getCode(), "该标签未维护!", null);
        }
        return ResponseData.success(meSfcWipLogService.getStationRecord(sn));
    }

    @GetMapping("/getShopOrderInfo")
    @ApiOperation("SN-工单信息")
    public ResponseData getShopOrderInfo(@RequestParam("sn") String sn) {
        return ResponseData.success(meSfcService.getShopOrderInfo(sn));
    }

    @GetMapping("/getRouterInfo")
    @ApiOperation("SN-工艺路线信息")
    public ResponseData getRouterInfo(@RequestParam("sn") String sn) {
        // TODO: nextOperation
        return ResponseData.success(meSfcService.getRouterInfo(sn));
    }

    //SN生命周期-检测记录
    @GetMapping("/getDetectionLifeCycleInfo")
    @ApiOperation("SN-检测记录")
    public ResponseData<List<DetectionSnInfoVo>> getDetectionLifeCycleInfo(@RequestParam("sn") String sn) {
        List<MeSfcNcLog> sfcNcLogs = meSfcNcLogService.list(new QueryWrapper<MeSfcNcLog>().eq("SFC", sn));

        if (CollUtil.isNotEmpty(sfcNcLogs)) {
            List<DetectionSnInfoVo> detectionLifeCycleInfo = meSfcMapper.getDetectionLifeCycleInfo(sn);

            // stationBo改为工位名
            if (CollUtil.isNotEmpty(detectionLifeCycleInfo)) {
                detectionLifeCycleInfo.forEach(l -> {
                    if (null != l.getStation()) {
                        StationHandleBO stationHandleBO = new StationHandleBO(l.getStation());
                        l.setStation(stationHandleBO.getStation());
                    }
                });
            }
            return ResponseData.success(detectionLifeCycleInfo);
        } else {
            List<DetectionSnInfoVo> detectionLifeCycleInfoGood = meSfcMapper.getDetectionLifeCycleInfoGood(sn);

            // stationBo改为工位名
            if (CollUtil.isNotEmpty(detectionLifeCycleInfoGood)) {
                detectionLifeCycleInfoGood.forEach(l -> {
                    if (null != l.getStation()) {
                        StationHandleBO stationHandleBO = new StationHandleBO(l.getStation());
                        l.setStation(stationHandleBO.getStation());
                    }
                });
            }
            return ResponseData.success(detectionLifeCycleInfoGood);
        }
    }

    //SN生命周期-维修记录
    @GetMapping("/getRepairLifeCycle")
    @ApiOperation("SN-维修记录")
    public ResponseData<List<RepairSnInfoVo>> getRepairLifeCycle(@RequestParam("sn") String sn) {
        List<RepairSnInfoVo> repairLifeCycle = meSfcMapper.getRepairLifeCycle(sn);
        if (CollUtil.isNotEmpty(repairLifeCycle)) {
            repairLifeCycle.forEach(r -> {
                if (null != r.getBadStation()) {
                    StationHandleBO stationHandleBO = new StationHandleBO(r.getBadStation());
                    r.setBadStation(stationHandleBO.getStation());
                }
                if (null != r.getRepairStation()) {
                    StationHandleBO stationHandleBO = new StationHandleBO(r.getRepairStation());
                    r.setRepairStation(stationHandleBO.getStation());
                }
            });
        }
        return ResponseData.success(repairLifeCycle);
    }


}
