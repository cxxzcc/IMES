package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ProductionCollectionRecordKey;

import java.util.List;

/**
 * 生产采集记录-关键件 服务
 * @author dengou
 * @date 2021/11/11
 */
public interface ProductionCollectionRecordKeyService extends IService<ProductionCollectionRecordKey> {

    /**
     * 关键件列表
     * @param id 采集记录id
     * @return 关键件列表
     * */
    List<ProductionCollectionRecordKey> getListByCollectionRecordId(String id);
}
