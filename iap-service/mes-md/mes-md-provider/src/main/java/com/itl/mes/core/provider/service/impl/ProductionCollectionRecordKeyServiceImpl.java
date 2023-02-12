package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.ProductionCollectionRecordKey;
import com.itl.mes.core.api.service.ProductionCollectionRecordKeyService;
import com.itl.mes.core.provider.mapper.ProductionCollectionRecordKeyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生产采集记录-关键件 服务实现
 * @author dengou
 * @date 2021/11/11
 */
@Service
public class ProductionCollectionRecordKeyServiceImpl extends ServiceImpl<ProductionCollectionRecordKeyMapper, ProductionCollectionRecordKey> implements ProductionCollectionRecordKeyService {

    @Override
    public List<ProductionCollectionRecordKey> getListByCollectionRecordId(String id) {
        return baseMapper.getListByCollectionRecordId(id);
    }
}
