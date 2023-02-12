package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import com.itl.mes.me.api.dto.RepairInputDto;
import com.itl.mes.me.api.dto.RepairTempDto;
import com.itl.mes.me.api.dto.ScrapOrRepairFinDto;
import com.itl.mes.me.api.service.MeSfcRepairService;
import com.itl.mes.me.api.vo.RepairObjVo;
import com.itl.mes.me.api.vo.RepairStationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
@Api(tags = "维修接口")
public class RepairController {

    @Autowired
    private MeSfcRepairService meSfcRepairService;

    @ApiOperation(value = "维修工位-扫描SN并带出:1成品上游信息2送修明细")
    @GetMapping("/getStationInformation")
    public ResponseData<RepairStationVo> getStationInform(String param) throws CommonException {
        return ResponseData.success(meSfcRepairService.frontDataVo(param));
    }

    @ApiOperation(value = "维修工位-维修录入界面")
    @PostMapping("/RepairStation/inputRepairData")
    public ResponseData<RepairObjVo> saveInputRepair(@RequestBody List<RepairInputDto> repairInputList) throws CommonException {
        return ResponseData.success(meSfcRepairService.saveInputRepair(repairInputList));
    }

    @ApiOperation(value = "维修工位-报废或维修完成")
    @PostMapping("/RepairStation/scrapOrRepairFinish")
    public ResponseData<Integer> scrapOrRepairFinish(@RequestBody ScrapOrRepairFinDto scrapOrRepairFinDto) throws CommonException {
        return ResponseData.success(meSfcRepairService.scrapOrRepairFinish(scrapOrRepairFinDto));
    }


    @ApiOperation(value = "维修工位-扫描SN并带出:不合格代码列表")
    @GetMapping("/defectRecord/{sn}")
    public ResponseData<List<ProductionDefectRecord>> getProductionDefectRecordList(
            @ApiParam(name = "sn", value = "sn", required = true)
            @PathVariable("sn") String sn) {
        return ResponseData.success(meSfcRepairService.getListBySn(sn));
    }


    @ApiOperation(value = "维修工位-条码报废")
    @PostMapping("/scrapped/{sn}")
    public ResponseData<Boolean> scrapped(
            @ApiParam(name = "sn", value = "sn", required = true)
            @PathVariable("sn") String sn) {
        return ResponseData.success(meSfcRepairService.scrapped(sn));
    }

    /**
     * 暂存维修记录
     * */
    @ApiOperation(value = "维修工位-暂存结果")
    @PostMapping("/saveTemp/{sn}")
    public ResponseData saveTemp(
            @ApiParam(name = "sn", value = "sn", required = true)
            @PathVariable("sn") String sn, @RequestBody List<RepairTempDto> repairTempDtos) {
        return ResponseData.success(meSfcRepairService.saveTemp(sn, repairTempDtos));
    }

}
