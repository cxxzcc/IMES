package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.SparePartChangeRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.SparepartStorageCountStatisticsDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author dengou
 * @date 2021/9/17
 */
public interface SparePartStorageRecordMapper extends BaseMapper<SparePartStorageRecord> {

    /**
     * 根据备件id查询备件的出入库记录
     * @param queryPage 分页参数
     * @param params 查询参数
     * */
    List<SparePartStorageRecordDTO> getStorageRecordBySparePart(Page<SparePartStorageRecordDTO> queryPage, @Param("params") Map<String, Object> params);

    /**
     * 根据记录id查询出入库详情
     * @param id 记录id
     * */
    SparePartStorageRecordDTO detail(String id);

    /**
     * 根据关联单号查询出入库详情列表
     * @param referenceOrderNo 关联单号
     * @return 出入库记录详情列表
     * */
    List<SparePartStorageRecordDTO> detailListByReferenceId(String referenceOrderNo);

    /**
     * 统计备件库存不足和库存过量的数量
     * */
    SparepartStorageCountStatisticsDTO storageCountStatistics(String site);

    /**
     * 备件更换记录
     * @param params code=设备编号；分页参数
     * @return 备件更换记录列表
     * */
    List<SparePartChangeRecordDTO> sparePartChangeRecord(Page<SparePartChangeRecordDTO> page, @Param("params") Map<String, Object> params);
}
