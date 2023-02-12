package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ProductMaintenanceMethodRecord;

import java.util.List;

/**
 * 维修记录-维修措施
 * @author dengou
 * @date 2021/11/11
 */
public interface ProductMaintenanceRecordKeyService extends IService<ProductMaintenanceMethodRecord> {

    /**
     * 根据维修记录id查询维修措施列表
     * @param id 维修记录id
     * @return 维修措施列表
     * */
    List<ProductMaintenanceMethodRecord> getListByCollectionRecordId(String id);
}
