package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.entity.CollectionRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
public interface ICollectionRecordService extends IService<CollectionRecord> {


    /**
     * 分页查询采集记录
     * @param params 分页查询参数
     * @return 分页列表
     * */
    Page<CollectionRecord> getPage(Map<String, Object> params);

    /**
     * 保存采集记录-定性，定量检验
     * @return 返回结果NG/OK. 采集结果有一个为NG则返回NG, 全部为OK才返回OK
     * */
    String saveTempQualitativeInspection(List<QualitativeInspectionSaveDTO> qualitativeInspectionSaveDTO, String shopOrder, String site);

    /**
     * 保存采集记录-维修记录
     * */
    Boolean saveProductMaintenanceRecord(List<RepairTempDataDTO> repairTempDataDTOS);
    /**
     * 保存采集记录, 非检验记录保存可用
     * @param repairTempDataDTOS 采集记录
     * */
    Boolean saveByCommon(CollectionRecordCommonTempDTO repairTempDataDTOS);
    /**
     * 保存采集记录, 非检验记录保存可用
     * @param repairTempDataDTOS 采集记录
     * */
    Boolean saveByCommons(List<CollectionRecordCommonTempDTO> repairTempDataDTOS);

    /**
     * 更新下工序
     * @param snBos 条码bo
     * @param operationBo 工序bo
     * @param operationName 工序名称
     * @return 是否成功
     * */
    Boolean updateNextOperation(List<String> snBos, String operationBo, String operationName);
}
