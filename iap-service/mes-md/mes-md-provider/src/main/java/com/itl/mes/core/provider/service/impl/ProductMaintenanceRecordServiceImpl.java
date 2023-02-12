package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.ProductMaintenanceRecord;
import com.itl.mes.core.api.service.ProductMaintenanceRecordService;
import com.itl.mes.core.provider.mapper.ProductMaintenanceRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品维修记录服务实现类
 * @author dengou
 * @date 2021/11/11
 */
@Service
public class ProductMaintenanceRecordServiceImpl extends ServiceImpl<ProductMaintenanceRecordMapper, ProductMaintenanceRecord> implements ProductMaintenanceRecordService {


    @Override
    public List<ProductMaintenanceRecord> getListByCollectionRecordId(String id) {
        return lambdaQuery().eq(ProductMaintenanceRecord::getCollectionRecordId, id).orderByDesc(ProductMaintenanceRecord::getCreateTime).list();
    }
}
