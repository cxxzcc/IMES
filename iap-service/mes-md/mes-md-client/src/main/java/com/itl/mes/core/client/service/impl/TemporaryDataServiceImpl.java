package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.client.service.TemporaryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dengou
 * @date 2021/11/12
 */
@Component
@Slf4j
public class TemporaryDataServiceImpl implements TemporaryDataService {
    @Override
    public ResponseData<List<QualitativeInspectionSaveDTO>> getQualitativeInspectionListBySn(String sn, String station) {
        log.error("sorry TemporaryDataService getQualitativeInspectionListBySn feign fallback sn:{}, station:{} ", sn, station);
        return ResponseData.error("getQualitativeInspectionListBySn调用失败");
    }

    @Override
    public ResponseData<List<QualitativeInspectionSaveDTO>> getQuantifyInspectionListBySn(String sn, String station) {
        log.error("sorry TemporaryDataService getQuantifyInspectionListBySn feign fallback sn:{}, station:{} ", sn, station);
        return ResponseData.error("getQuantifyInspectionListBySn调用失败");
    }

    @Override
    public ResponseData<Boolean> remove(String sn, String station, String type) {
        log.error("sorry TemporaryDataService remove feign fallback sn:{}, station:{}，type:{} ", sn, station, type);
        return ResponseData.error("remove调用失败");
    }

    @Override
    public ResponseData<Boolean> removeList(String sn, String station, String types) {
        log.error("sorry TemporaryDataService remove feign fallback sn:{}, station:{}，type:{} ", sn, station, types);
        return ResponseData.error("remove调用失败");
    }

    @Override
    public ResponseData<List<CollectionRecordCommonTempDTO>> getCommons(String sn, String station, String types) {
        log.error("sorry TemporaryDataService remove feign fallback sn:{}, station:{}，type:{} ", sn, station, types);
        return ResponseData.error("getCommons调用失败");
    }

    @Override
    public ResponseData<Boolean> addOrUpdate(TemporaryData temporaryData) {
        log.error("sorry TemporaryDataService addOrUpdate feign fallback temporaryData", temporaryData);
        return ResponseData.error("addOrUpdate调用失败");
    }

    @Override
    public ResponseData<List<RepairTempDataDTO>> getRepairTempDataDtoList(String sn, String station) {
        log.error("sorry TemporaryDataService getRepairTempDataDtoList feign fallback sn：{}，station:{}", sn, station);
        return ResponseData.error("getRepairTempDataDtoList调用失败");
    }

    @Override
    public ResponseData<List<TemporaryData>> getByIds(List<String> ids) {
        log.error("sorry TemporaryDataService getByIds feign fallback ids：{}", ids);
        return ResponseData.error("getByIds调用失败");
    }
}
