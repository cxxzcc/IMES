package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.dto.UpdateNextOperationDto;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import com.itl.mes.core.client.service.CollectionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author dengou
 * @date 2021/11/12
 */
@Slf4j
@Service
public class CollectionRecordServiceImpl implements CollectionRecordService {

    @Override
    public ResponseData<String> saveTempQualitativeInspection(List<QualitativeInspectionSaveDTO> qualitativeInspectionSaveDTO, String shopOrder, String site) {
        log.error("sorry CollectionRecordService saveByTemp feign fallback qualitativeInspectionSaveDTO:{},shopOrder:{}, site:{} ", qualitativeInspectionSaveDTO.toString(), shopOrder, site);
        return ResponseData.error("saveByTemp调用失败");
    }

    @Override
    public ResponseData<Boolean> saveProductMaintenanceRecord(List<RepairTempDataDTO> repairTempDataDTOS) {
        log.error("sorry CollectionRecordService saveProductMaintenanceRecord feign fallback repairTempDataDTOS:{}", repairTempDataDTOS);
        return ResponseData.error("saveProductMaintenanceRecord调用失败");
    }

    @Override
    public ResponseData<List<ProductionDefectRecord>> getDefectRecordListBySn(String sn) {
        log.error("sorry CollectionRecordService getListBySn feign fallback sn:{} ", sn);
        return ResponseData.error("getListBySn调用失败");
    }

    @Override
    public ResponseData<List<ProductionDefectRecord>> getDefectRecordListByIds(List<String> ids) {
        log.error("sorry CollectionRecordService getDefectRecordListByIds feign fallback ids:{} ", ids);
        return ResponseData.error("getDefectRecordListByIds调用失败");
    }

    @Override
    public ResponseData<Boolean> saveByCommon(List<CollectionRecordCommonTempDTO> collectionRecordCommonTempDTOS) {
        log.error("sorry CollectionRecordService saveByCommon feign fallback collectionRecordCommonTempDTOS:{} ", collectionRecordCommonTempDTOS);
        return ResponseData.error("saveByCommon调用失败");
    }

    @Override
    public ResponseData<Boolean> updateNextOperation(@RequestBody UpdateNextOperationDto body) {
        log.error("sorry CollectionRecordService updateNextOperation feign fallback body:{} ", body);
        return ResponseData.error("updateNextOperation调用失败");
    }
}
