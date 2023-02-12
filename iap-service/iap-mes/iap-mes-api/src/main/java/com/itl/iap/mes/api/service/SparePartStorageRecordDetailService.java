package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecordDetail;

import java.util.Map;

/**
 * 备件出入库详情
 * @author dengou
 * @date 2021/9/22
 */
public interface SparePartStorageRecordDetailService extends IService<SparePartStorageRecordDetail> {

    /**
     * 出入库备件列表
     * @param params recordId 出入库记录id
     * */
    Page<SparePartStorageRecordDetail> listByRecordId(Map<String, Object> params);
}
