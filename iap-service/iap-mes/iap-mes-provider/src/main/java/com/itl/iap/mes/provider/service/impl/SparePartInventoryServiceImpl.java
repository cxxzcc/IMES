package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.mes.api.entity.sparepart.SparePartInventory;
import com.itl.iap.mes.api.service.SparePartInventoryService;
import com.itl.iap.mes.provider.mapper.SparePartInventoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 备件库存服务实现
 * @author dengou
 * @date 2021/9/22
 */
@Service
public class SparePartInventoryServiceImpl extends ServiceImpl<SparePartInventoryMapper, SparePartInventory> implements SparePartInventoryService {

    @Override
    public List<SparePartInventory> listByWareHouseIdAndSparePartId(String wareHouseId, Set<String> sparePartIds) {
        return lambdaQuery().eq(SparePartInventory::getWareHouseId, wareHouseId)
                .in(SparePartInventory::getSparePartId, sparePartIds).list();
    }

    @Override
    public List<SparePartInventory> listBySparePartId(String sparePartId) {
        return lambdaQuery().eq(SparePartInventory::getSparePartId, sparePartId).list();
    }

    @Override
    public List<SparePartInventory> listBySparePartIds(Set<String> sparePartIds) {
        return lambdaQuery().in(SparePartInventory::getSparePartId, sparePartIds).list();
    }
}
