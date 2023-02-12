package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.mes.api.dto.SparePartChangeRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageRequestDTO;
import com.itl.iap.mes.api.dto.sparepart.SparepartStorageCountStatisticsDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecord;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecordDetail;

import java.util.List;
import java.util.Map;

/**
 * 备件出入库记录服务接口
 * @author dengou
 * @date 2021/9/22
 */
public interface SparePartStorageRecordService  extends IService<SparePartStorageRecord> {

    /**
     * 备件出入库记录
     * @param params id:备件id, page,limit
     * @return 备件出入库记录，按时间倒序排序
     * */
    Page<SparePartStorageRecordDTO> getStorageRecordBySparePart(Map<String, Object> params);

    /**
     * 出库/入库
     * @param sparePartStorageRecord 出库/入库信息
     * @return 是否成功
     * */
    Boolean addStorageRecord(SparePartStorageRecord sparePartStorageRecord) throws CommonException;
    /**
     * 出库/入库
     * @param sparePartStorageRequestDTO 出库/入库信息
     * @return 是否成功
     * */
    Boolean addStorageRecord(SparePartStorageRequestDTO sparePartStorageRequestDTO) throws CommonException;

    /**
     * 出入库记录详情
     * @param id 出入库记录id
     * @return 出入库记录详情
     * */
    SparePartStorageRecordDTO detail(String id);

    /**
     * 根据关联单号查询出入库详情列表
     * @param referenceOrderNo 关联单号
     * @return 出入库记录详情列表
     * */
    List<SparePartStorageRecordDTO> detailListByReferenceNo(String referenceOrderNo);

    /**
     * 维修领料， 库存不足将会抛出异常{@link CommonException}
     * @param sparePartStorageRequestDTO 出库详情
     * @return 是否成功
     * */
    Boolean outByMaintenance(SparePartStorageRequestDTO sparePartStorageRequestDTO) throws CommonException;

    /**
     * 统计备件库存不足和库存过量的数量
     * @param site 工厂
     * */
    SparepartStorageCountStatisticsDTO storageCountStatistics(String site);

    /**
     * 备件更换记录
     * @param params code=设备编号；分页参数
     * @return 备件更换记录列表
     * */
    Page<SparePartChangeRecordDTO> sparePartChangeRecord(Map<String, Object> params);

    /**
     * 出入库备件列表
     * @param params recordId 出入库记录id
     * */
    Page<SparePartStorageRecordDetail> listByRecordId(Map<String, Object> params);
}
