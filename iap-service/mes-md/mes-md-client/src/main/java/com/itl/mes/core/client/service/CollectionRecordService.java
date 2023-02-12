package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.dto.UpdateNextOperationDto;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.CollectionRecordServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author dengou
 * @date 2021/11/12
 */
@FeignClient(value = "mes-md-provider",contextId = "collectionRecord", fallback = CollectionRecordServiceImpl.class, configuration = FallBackConfig.class)
public interface CollectionRecordService {


    /**
     * 保存采集记录-定性，定量检验
     * */
    @PostMapping("/collectionRecord/saveTempQualitativeInspection")
    ResponseData<String> saveTempQualitativeInspection(@RequestBody List<QualitativeInspectionSaveDTO> qualitativeInspectionSaveDTO, @RequestParam("shopOrder") String shopOrder, @RequestParam("site") String site);
    /**
     * 保存采集记录-维修
     * @param repairTempDataDTOS 维修记录
     * */
    @PostMapping("/collectionRecord/saveProductMaintenanceRecord")
    ResponseData<Boolean> saveProductMaintenanceRecord(@RequestBody List<RepairTempDataDTO> repairTempDataDTOS);


    /**
     * 根据sn查询不合格代码列表
     * @param sn sn
     * @return 缺陷记录列表
     * */
    @PostMapping("/collectionRecord/defectRecord/getListBySn")
    ResponseData<List<ProductionDefectRecord>> getDefectRecordListBySn(@RequestParam("sn") String sn);
    /**
     * 根据id列表查询不合格代码列表
     * @param ids id列表
     * @return 缺陷记录列表
     * */
    @PostMapping("/collectionRecord/defectRecord/getListByIds")
    ResponseData<List<ProductionDefectRecord>> getDefectRecordListByIds(@RequestBody List<String> ids);


    /**
     * 保存采集记录-通用方法
     * @param collectionRecordCommonTempDTOS 通用采集记录
     * */
    @PostMapping("/collectionRecord/saveByCommons")
    ResponseData<Boolean> saveByCommon(@RequestBody List<CollectionRecordCommonTempDTO> collectionRecordCommonTempDTOS);

    /**
     * 更新下工序
     * @param snBoListStr 条码bo
     * @param operationBo 工序bo
     * @param operationName 工序名称
     * @return 是否成功
     * */
    @PostMapping("/collectionRecord/updateNextOperation")
    ResponseData<Boolean> updateNextOperation(@RequestBody UpdateNextOperationDto body);
}
