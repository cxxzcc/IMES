package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecordDetail;
import com.itl.iap.mes.api.service.SparePartStorageRecordDetailService;
import com.itl.iap.mes.provider.mapper.SparePartStorageRecordDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 备件库存记录详情实现
 * @author dengou
 * @date 2021/9/22
 */
@Service
public class SparePartStorageRecordDetailServiceImpl extends ServiceImpl<SparePartStorageRecordDetailMapper, SparePartStorageRecordDetail> implements SparePartStorageRecordDetailService {
    @Override
    public Page<SparePartStorageRecordDetail> listByRecordId(Map<String, Object> params) {
        Page<SparePartStorageRecordDetail> page = new QueryPage<>(params);
        List<SparePartStorageRecordDetail> list = baseMapper.listByRecordId(page, params);
        page.setRecords(list);
        return page;
    }
}
