package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.service.ITemporaryDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 定量检验controller
 * @author dengou
 * @date 2021/11/16
 */
@Slf4j
@RestController
@RequestMapping("/repair")
@Api(tags = "维修")
public class MaintenanceMethodRecordController {


    @Autowired
    private ITemporaryDataService temporaryDataService;


    @GetMapping("/getBySn/{sn}")
    @ApiOperation(value = "根据sn查询定性检验暂存数据")
    public ResponseData<List<RepairTempDataDTO>> getBySn(@PathVariable("sn") String sn, @RequestParam("station") String station) {
        TemporaryData temporaryData = temporaryDataService.getBySnAndStation(sn, station, TemporaryDataTypeEnum.REPAIR.getCode());
        if(ObjectUtil.isNull(temporaryData)) {
            return ResponseData.success(Collections.emptyList());
        }
        String content = temporaryData.getContent();
        if(StrUtil.isBlank(content)) {
            return ResponseData.success(Collections.emptyList());
        }
        return ResponseData.success(JSONUtil.parseArray(content).toList(RepairTempDataDTO.class));
    }


}
