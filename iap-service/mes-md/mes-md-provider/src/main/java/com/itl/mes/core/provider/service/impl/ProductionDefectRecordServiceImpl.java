package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import com.itl.mes.core.api.service.IProductionDefectRecordService;
import com.itl.mes.core.provider.mapper.ProductionDefectRecordMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Service
public class ProductionDefectRecordServiceImpl extends ServiceImpl<ProductionDefectRecordMapper, ProductionDefectRecord> implements IProductionDefectRecordService {


    @Override
    public List<ProductionDefectRecord> getListByCollectionRecordId(String collectionRecordId) {
        return lambdaQuery().eq(ProductionDefectRecord::getCollectionRecordId, collectionRecordId).orderByDesc(ProductionDefectRecord::getCreateTime).list();
    }

    @Override
    public List<ProductionDefectRecord> getListBySn(String sn) {
        return baseMapper.getListBySn(sn);
    }

    @Override
    public List<ProductionDefectRecord> getListByIds(List<String> ids) {
        if(CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(listByIds(ids));
    }

    @Override
    public Boolean updateHandleFlag(List<String> defectRecordIds) {
        if(CollUtil.isEmpty(defectRecordIds)) {
            return false;
        }
        return baseMapper.updateHandleFlag(defectRecordIds) > 0;
    }
}
