package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.mes.api.entity.sparepart.SparePartInventory;

import java.util.List;
import java.util.Set;

/**
 * 备件库存service
 * @author dengou
 * @date 2021/9/22
 */
public interface SparePartInventoryService extends IService<SparePartInventory> {

    /**
     * 根据仓库id和备件id查询库存信息
     * @param sparePartIds 备件id
     * @param wareHouseId 仓库id
     * @return 返回库存信息列表
     * */
    List<SparePartInventory> listByWareHouseIdAndSparePartId(String wareHouseId, Set<String> sparePartIds);


    /**
     * 根据备件id查询仓储信息列表
     * @param sparePartId 备件id
     * @return 返回库存信息列表
     * */
    List<SparePartInventory> listBySparePartId(String sparePartId);
    /**
     * 根据备件ids查询仓储信息列表
     * @param sparePartIds 备件id
     * @return 返回库存信息列表
     * */
    List<SparePartInventory> listBySparePartIds(Set<String> sparePartIds);
}
