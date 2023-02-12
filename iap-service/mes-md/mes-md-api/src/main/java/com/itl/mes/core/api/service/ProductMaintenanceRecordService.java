package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ProductMaintenanceRecord;

import java.util.List;

/**
 * 产品维修记录service
 * @author dengou
 * @date 2021/11/11
 */
public interface ProductMaintenanceRecordService extends IService<ProductMaintenanceRecord> {

    /**
     * 产品维修记录
     * @param id 采集记录id
     * @return 产品维修记录
     * */
    List<ProductMaintenanceRecord> getListByCollectionRecordId(String id);
}
