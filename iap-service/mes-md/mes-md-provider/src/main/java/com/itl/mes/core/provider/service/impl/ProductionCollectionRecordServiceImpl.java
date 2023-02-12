package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.constant.ProductionCollectionRecordStateEnum;
import com.itl.mes.core.api.entity.ProductionCollectionRecord;
import com.itl.mes.core.api.service.ProductionCollectionRecordService;
import com.itl.mes.core.provider.mapper.ProductionCollectionRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生产采集记录服务实现类
 * @author dengou
 * @date 2021/11/11
 */
@Service
public class ProductionCollectionRecordServiceImpl extends ServiceImpl<ProductionCollectionRecordMapper, ProductionCollectionRecord> implements ProductionCollectionRecordService {

    @Override
    public List<ProductionCollectionRecord> getListByCollectionRecordId(String id) {
        List<ProductionCollectionRecord> list = lambdaQuery().eq(ProductionCollectionRecord::getCollectionRecordId, id).orderByDesc(ProductionCollectionRecord::getOperationTime).list();
        if(CollUtil.isNotEmpty(list)) {
            list.forEach(e -> {
                e.setStateDesc(ProductionCollectionRecordStateEnum.parseDescByCode(e.getState()));
            });
        }
        return list;
    }

}
