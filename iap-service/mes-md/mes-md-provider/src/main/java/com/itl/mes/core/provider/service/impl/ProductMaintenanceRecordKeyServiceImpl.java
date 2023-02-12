package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.ProductMaintenanceMethodRecord;
import com.itl.mes.core.api.service.ProductMaintenanceRecordKeyService;
import com.itl.mes.core.provider.mapper.ProductMaintenanceRecordKeyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 维修记录-维修措施服务实现类
 * @author dengou
 * @date 2021/11/11
 */
@Service
public class ProductMaintenanceRecordKeyServiceImpl extends ServiceImpl<ProductMaintenanceRecordKeyMapper, ProductMaintenanceMethodRecord> implements ProductMaintenanceRecordKeyService {

    @Override
    public List<ProductMaintenanceMethodRecord> getListByCollectionRecordId(String id) {
        return lambdaQuery().eq(ProductMaintenanceMethodRecord::getProductionMaintenanceRecordId, id).list();
    }

}
