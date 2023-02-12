package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ProductionCollectionRecord;

import java.util.List;

/**
 * 生产采集记录 服务接口
 * @author dengou
 * @date 2021/11/11
 */
public interface ProductionCollectionRecordService extends IService<ProductionCollectionRecord> {

    /**
     * 生产采集记录
     * @param id 采集记录id
     * @return 采集记录列表
     * */
    List<ProductionCollectionRecord> getListByCollectionRecordId(String id);
}
