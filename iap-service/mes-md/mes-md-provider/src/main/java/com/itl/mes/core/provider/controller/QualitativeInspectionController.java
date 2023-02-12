package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.QualitativeInspectionDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.TempDateSaveRequestDto;
import com.itl.mes.core.provider.service.impl.QualitativeInspectionServiceImpl;
import com.itl.mes.me.api.dto.MeProductInspectionItemsOrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname StationQualitativeInspection
 * @Description TODO
 * @Date 2021/11/1 15:33
 * @Created by 侯凡
 */
@Slf4j
@RestController
@RequestMapping("/qualitativeInspection")
@Api(tags = "工位定性检验")
public class QualitativeInspectionController {

    @Autowired
    private QualitativeInspectionServiceImpl qualitativeInspectionService;

    /**
     * 查询当前工位编码(改为前端传入)
     * 工位编码 去【工位维护】表 获取工序编码
     *
     * 根据SN 去 【工单标签】的条形码【条码标】表中查询 工单编码
     *
     * 根据 工单编码 去【工单维护】的【检验项目】标签并过滤  拿到相关信息
     * 	过滤条件：
     * 		工序+检验标识
     * 		（工序编码）+ （定性）
     */
    @PostMapping("/searchTable")
    @ApiOperation(value = "根据验证条码SN查询相关定性检查信息")
    @ApiResponse(
            code = 200, message = "success",
            response = MeProductInspectionItemsOrderDto.class, responseContainer = "List")
    public ResponseData<List<MeProductInspectionItemsOrderDto>> searchTable(@RequestBody QualitativeInspectionDTO dto) {
        log.info("QualitativeInspectionController--searchTable param:{}", dto);
        return ResponseData.success(qualitativeInspectionService.getProductInspectionItems(dto));
    }

    @PostMapping("/temporarySave/{sn}")
    @ApiOperation(value = "提交暂存")
    public ResponseData<Boolean> temporarySave(@RequestBody List<TempDateSaveRequestDto> list,
                                               @ApiParam(name = "sn", value = "sn", required = true) @PathVariable("sn") String sn) {
        return ResponseData.success(qualitativeInspectionService.saveQualitativeInspection(list, sn));
    }

    @GetMapping("/getBySn/{sn}")
    @ApiOperation(value = "根据sn查询定性检验暂存数据")
    public ResponseData<List<QualitativeInspectionSaveDTO>> getBySn(@PathVariable("sn") String sn, @RequestParam("station") String station) {
        return ResponseData.success(qualitativeInspectionService.getBySnAndStation(sn, station));
    }


}
