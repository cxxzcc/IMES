package com.itl.wms.md.api.service.sap;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.wms.md.api.dto.sap.*;
import com.itl.wms.md.api.vo.sap.InventoryVo;

import java.util.List;


/**
 * @author zhec
 * @create 2021/9/10
 * @description 调用sap接口
 **/
public interface CallSapApiService {


    /**
     * 生产入库
     *
     * @param productionStorageDto
     * @return
     */
    ResponseData productionStorage(ProductionStorageDto productionStorageDto);

    /**
     * 货物移动 物料凭证创建
     *
     * @param goodsMovementDto
     * @return
     */
    ResponseData goodsMovement(GoodsMovementDto goodsMovementDto);

    /**
     * 同步仓库数据
     *
     * @param syncWarehouseDto
     * @return
     */
    ResponseData syncWarehouse(SyncWarehouseDto syncWarehouseDto);


    /**
     * 销售发退货过账
     *
     * @param salePlayAndReturnDto
     * @return
     */
    ResponseData salePlayAndReturn(List<SalePlayAndReturnDto> salePlayAndReturnDto);


    /**
     * 移库单
     *
     * @param moveWarehouseMonadDto
     * @return
     */
    ResponseData moveWarehouseMonad(List<MoveWarehouseMonadDto> moveWarehouseMonadDto);


    /**
     * 查询库存
     *
     * @param inventoryDto
     * @return
     */
    ResponseData<List<InventoryVo>> findInventory(InventoryDto inventoryDto);

    /**
     * sap接口推送
     * @param ids
     * @return
     */
    ResponseData call(List<String> ids);

}
