package com.itl.wms.md.provider.service.impl.sap;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.LogUtil;
import com.itl.iap.common.util.DateUtil;
import com.itl.iap.common.util.HttpClient;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.entity.IapDictT;
import com.itl.iap.system.api.entity.IapOpsLogT;
import com.itl.wms.md.api.dto.sap.*;
import com.itl.wms.md.api.entity.Warehouse;
import com.itl.wms.md.api.service.WarehouseService;
import com.itl.wms.md.api.service.sap.CallSapApiService;
import com.itl.wms.md.api.vo.sap.InventoryVo;
import com.itl.wms.md.provider.mapper.WarehouseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhec
 * @create 2021/9/10
 * @description 调用sap接口实现类
 **/
@Slf4j
@Service
public class CallSapApiServiceImpl implements CallSapApiService {

    @Value("${sap.url}")
    private String url;

    @Value("${sap.authorized.username}")
    private String username;

    @Value("${sap.authorized.password}")
    private String password;

    public static final String PRODUCTION_STORAGE_ROUTING = "/Warehouse/CreateMTSRestPS";

    public static final String GOODS_MOVEMENT_ROUTING = "/Material/CreateGoodSMVTRestPS";

    public static final String WAREHOUSE_ROUTING = "/Warehouse/GetWarehouseRestPS";

    public static final String SALE_PLAY_AND_RETURN_ROUTING = "/Warehouse/SalesIssueReturnRestPS";

    public static final String MOVE_WAREHOUSE_MONAD_ROUTING = "/Warehouse/TransferLibraryRestPS";

    public static final String FIND_INVENTORY_ROUTING = "/Warehouse/InventoryQueryRestPS";

    public static final String SALE_CODE = "SaleSilentPeriodSet";

    public static final String PRODUCE_CODE = "SilentPeriodSet";

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private LogUtil logUtil;

    @Autowired
    private WarehouseMapper warehouseMapper;


    @Override
    public ResponseData productionStorage(ProductionStorageDto productionStorageDto) {
        log.info("params:{}", JSON.toJSONString(productionStorageDto));
        JSONObject rseObject = null;
        try {
            if (querySilentPeriodSet(JSON.toJSONString(productionStorageDto), url + PRODUCTION_STORAGE_ROUTING, "1", PRODUCE_CODE)) {
                return ResponseData.success("静默期开启延缓执行!");
            }
            String resStr = HttpClient.doPost(url + PRODUCTION_STORAGE_ROUTING, username, password, JSON.toJSONString(productionStorageDto));
            rseObject = JSONObject.parseObject(resStr);
            if (!"S".equals(rseObject.getString("RTNCODE"))) {
                logUtil.log("SAP", JSON.toJSONString(productionStorageDto), url + PRODUCTION_STORAGE_ROUTING, rseObject.getString("RTNMSG"), "1", "1", "1");
                return ResponseData.error(rseObject.getString("RTNMSG"));
            }
        } catch (Exception e) {
            logUtil.log("SAP", JSON.toJSONString(productionStorageDto), url + PRODUCTION_STORAGE_ROUTING, e.getMessage(), "2", "1", "1");
            return ResponseData.error(e.getMessage());
        }
        logUtil.log("SAP", JSON.toJSONString(productionStorageDto), url + PRODUCTION_STORAGE_ROUTING, rseObject.getString("RTNMSG"), "1", "1", "0");
        return ResponseData.success(rseObject.getString("RTNMSG"));

    }

    @Override
    public ResponseData goodsMovement(GoodsMovementDto goodsMovementDto) {
        log.info("params:{}", JSON.toJSONString(goodsMovementDto));
        JSONObject rseObject = null;
        try {
            if (querySilentPeriodSet(JSON.toJSONString(goodsMovementDto), url + GOODS_MOVEMENT_ROUTING, "2", PRODUCE_CODE)) {
                return ResponseData.success("静默期开启延缓执行!");
            }
            String resStr = HttpClient.doPost(url + GOODS_MOVEMENT_ROUTING, username, password, JSON.toJSONString(goodsMovementDto));
            rseObject = JSONObject.parseObject(resStr);
            if (!"S".equals(rseObject.getString("RTNCODE"))) {
                logUtil.log("SAP", JSON.toJSONString(goodsMovementDto), url + GOODS_MOVEMENT_ROUTING, rseObject.getString("RTNMSG"), "1", "2", "1");
                return ResponseData.error(rseObject.getString("RTNMSG"));
            }
        } catch (Exception e) {
            logUtil.log("SAP", JSON.toJSONString(goodsMovementDto), url + GOODS_MOVEMENT_ROUTING, e.getMessage(), "2", "2", "1");
            return ResponseData.error(e.getMessage());
        }
        logUtil.log("SAP", JSON.toJSONString(goodsMovementDto), url + GOODS_MOVEMENT_ROUTING, rseObject.getString("RTNMSG"), "1", "2", "0");
        return ResponseData.success(rseObject.getString("RTNMSG"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData syncWarehouse(SyncWarehouseDto syncWarehouseDto) {
        log.info("params:{}", JSON.toJSONString(syncWarehouseDto));
        try {
            String resStr = HttpClient.doPost(url + WAREHOUSE_ROUTING, username, password, JSON.toJSONString(syncWarehouseDto));
            JSONObject rseObject = JSONObject.parseObject(resStr);
            if (!"S".equals(rseObject.getString("RTNCODE"))) {
                logUtil.log("SAP", JSON.toJSONString(syncWarehouseDto), url + WAREHOUSE_ROUTING, rseObject.getString("RTNMSG"), "1", "7", "1");
                return ResponseData.error(rseObject.getString("RTNMSG"));
            }
            JSONArray warhouseList = rseObject.getJSONObject("ET_WAREHOUSE").getJSONArray("item");
            List<Warehouse> saveData = new ArrayList<>();
            for (int i = 0; i < warhouseList.size(); i++) {
                JSONObject item = warhouseList.getJSONObject(i);
                Warehouse warehouse = new Warehouse();
                warehouse.setBo(UUID.uuid32());
                warehouse.setSite(item.getString("WERKS"));
                warehouse.setWarehouseCode(item.getString("LGORT"));
                warehouse.setWarehouseName(item.getString("LGOBE"));
                warehouse.setWarehouseDesc(item.getString("LGOBE"));
                warehouse.setReserved1(item.getString("SPRAE01"));
                warehouse.setReserved2(item.getString("SPRAE02"));
                warehouse.setReserved3(item.getString("SPRAE03"));
                warehouse.setReserved4(item.getString("SPRAE04"));
                warehouse.setReserved5(item.getString("SPRAE05"));
                warehouse.setReserved6(item.getString("SPRAE06"));
                warehouse.setReserved7(item.getString("SPRAE07"));
                warehouse.setReserved8(item.getString("SPRAE08"));
                warehouse.setReserved9(item.getString("SPRAE09"));
                warehouse.setReserved10(item.getString("SPRAE10"));
                //设置默认值
                //仓库编码开头为2为线边仓否则为成品仓
                if (item.getString("LGORT").startsWith("2")) {
                    warehouse.setWarehouseCategory("12");
                } else {
                    warehouse.setWarehouseCategory("2");
                }
                warehouse.setOnhandFlag("Y");
                warehouse.setNegativeFlag("N");
                warehouse.setAreaFlag("Y");
                warehouse.setUnitFlag("N");
                warehouse.setTagFlag("Y");
                warehouse.setLotFlag("Y");
                warehouse.setPlanFlag("N");
                warehouse.setEnabledFlag("Y");
                saveData.add(warehouse);
            }
            List<Warehouse> addData = new ArrayList<>();
            List<Warehouse> updData = new ArrayList<>();
            Date currentDate = new Date();
            List<Warehouse> oldData = Collections.synchronizedList(warehouseService.list());
            saveData.stream().forEach(
                    data -> {
                        List<Warehouse> existData = oldData.parallelStream().filter(
                                old -> old.getSite().equals(data.getSite()) && old.getWarehouseCode().equals(data.getWarehouseCode())
                        ).collect(Collectors.toList());
                        Warehouse warehouse;
                        if (CollectionUtil.isNotEmpty(existData)) {
                            warehouse = data;
                            warehouse.setBo(existData.get(0).getBo());
                            warehouse.setModifyUser("SAP");
                            warehouse.setModifyDate(currentDate);
                            updData.add(warehouse);
                        } else {
                            warehouse = data;
                            warehouse.setCreateUser("SAP");
                            warehouse.setCreateDate(currentDate);
                            addData.add(warehouse);
                        }
                    }
            );
            if (CollectionUtil.isNotEmpty(addData)) {
                warehouseService.saveBatch(addData);
            }
            if (CollectionUtil.isNotEmpty(updData)) {
                warehouseService.updateBatchById(updData);
            }
        } catch (Exception e) {
            logUtil.log("SAP", JSON.toJSONString(syncWarehouseDto), url + WAREHOUSE_ROUTING, e.getMessage(), "2", "7", "1");
            return ResponseData.error(e.getMessage());
        }
        logUtil.log("SAP", JSON.toJSONString(syncWarehouseDto), url + WAREHOUSE_ROUTING, "执行成功!", "1", "7", "0");
        return ResponseData.success("success");
    }

    @Override
    public ResponseData salePlayAndReturn(List<SalePlayAndReturnDto> salePlayAndReturnDto) {
        log.info("params:{}", JSON.toJSONString(salePlayAndReturnDto));
        JSONObject rseObject = null;
        try {
            if (querySilentPeriodSet(JSON.toJSONString(salePlayAndReturnDto), url + SALE_PLAY_AND_RETURN_ROUTING, "4", SALE_CODE)) {
                return ResponseData.success("静默期开启延缓执行!");
            }
            Map<String, List<SalePlayAndReturnDto>> dtoMap = new HashMap<>();
            dtoMap.put("item", salePlayAndReturnDto);
            Map<String, Map<String, List<SalePlayAndReturnDto>>> paramMap = new HashMap<>();
            paramMap.put("IT_ZWSM09", dtoMap);
            String resStr = HttpClient.doPost(url + SALE_PLAY_AND_RETURN_ROUTING, username, password, JSON.toJSONString(paramMap));
            rseObject = JSONObject.parseObject(resStr);
            if (!"S".equals(rseObject.getString("O_STATUS"))) {
                logUtil.log("SAP", JSON.toJSONString(salePlayAndReturnDto), url + SALE_PLAY_AND_RETURN_ROUTING, rseObject.getString("O_MSG"), "1", "4", "1");
                return ResponseData.error(rseObject.getString("O_MSG"));
            }
        } catch (Exception e) {
            logUtil.log("SAP", JSON.toJSONString(salePlayAndReturnDto), url + SALE_PLAY_AND_RETURN_ROUTING, e.getMessage(), "2", "4", "1");
            return ResponseData.error(e.getMessage());
        }
        logUtil.log("SAP", JSON.toJSONString(salePlayAndReturnDto), url + SALE_PLAY_AND_RETURN_ROUTING, rseObject.getString("O_MSG"), "1", "4", "0");
        return ResponseData.success(rseObject.getString("O_MSG"));
    }

    @Override
    public ResponseData moveWarehouseMonad(List<MoveWarehouseMonadDto> moveWarehouseMonadDto) {
        log.info("params:{}", JSON.toJSONString(moveWarehouseMonadDto));
        JSONObject rseObject = null;
        try {
            if (querySilentPeriodSet(JSON.toJSONString(moveWarehouseMonadDto), url + MOVE_WAREHOUSE_MONAD_ROUTING, "3", PRODUCE_CODE)) {
                return ResponseData.success("静默期开启延缓执行!");
            }
            Map<String, List<MoveWarehouseMonadDto>> dtoMap = new HashMap<>();
            dtoMap.put("item", moveWarehouseMonadDto);
            Map<String, Map<String, List<MoveWarehouseMonadDto>>> paramMap = new HashMap<>();
            paramMap.put("I_ZITSOA08", dtoMap);
            String resStr = HttpClient.doPost(url + MOVE_WAREHOUSE_MONAD_ROUTING, username, password, JSON.toJSONString(paramMap));
            rseObject = JSONObject.parseObject(resStr);
            if (!"S".equals(rseObject.getString("O_STATUS"))) {
                logUtil.log("SAP", JSON.toJSONString(moveWarehouseMonadDto), url + MOVE_WAREHOUSE_MONAD_ROUTING, rseObject.getString("O_MSG"), "1", "3", "1");
                return ResponseData.error(rseObject.getString("O_MSG"));
            }
        } catch (Exception e) {
            logUtil.log("SAP", JSON.toJSONString(moveWarehouseMonadDto), url + MOVE_WAREHOUSE_MONAD_ROUTING, e.getMessage(), "2", "3", "1");
            return ResponseData.error(e.getMessage());
        }
        logUtil.log("SAP", JSON.toJSONString(moveWarehouseMonadDto), url + MOVE_WAREHOUSE_MONAD_ROUTING, rseObject.getString("O_MSG"), "1", "3", "0");
        return ResponseData.success(rseObject.getString("O_MSG"));
    }

    @Override
    public ResponseData<List<InventoryVo>> findInventory(InventoryDto inventoryDto) {
        log.info("params:{}", JSON.toJSONString(inventoryDto));
        JSONObject rseObject = null;
        List<InventoryVo> inventoryVoList = new ArrayList<>();
        try {
            //查询库存不受静默期参数影响
            String resStr = HttpClient.doPost(url + FIND_INVENTORY_ROUTING, username, password, JSON.toJSONString(inventoryDto));
            rseObject = JSONObject.parseObject(resStr);
            if (!"S".equals(rseObject.getString("O_STATUS"))) {
                logUtil.log("SAP", JSON.toJSONString(inventoryDto), url + FIND_INVENTORY_ROUTING, rseObject.getString("O_MSG"), "1", "8", "1");
                return ResponseData.error(rseObject.getString("O_MSG"));
            }
            JSONArray itemJsonList = rseObject.getJSONObject("O_ITAB").getJSONArray("item");
            inventoryVoList = itemJsonList.toJavaList(InventoryVo.class);
        } catch (Exception e) {
            logUtil.log("SAP", JSON.toJSONString(inventoryDto), url + FIND_INVENTORY_ROUTING, e.getMessage(), "2", "8", "1");
            return ResponseData.error(e.getMessage());
        }
        logUtil.log("SAP", JSON.toJSONString(inventoryDto), url + FIND_INVENTORY_ROUTING, rseObject.getString("O_MSG"), "1", "8", "0");
        return ResponseData.success(inventoryVoList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData call(List<String> ids) {
        List<IapOpsLogT> iapOpsLogTList = warehouseMapper.findLogById(ids);
        if (CollectionUtil.isNotEmpty(iapOpsLogTList)) {
            for (IapOpsLogT logT : iapOpsLogTList) {
                try {
                    Thread.sleep(5000);
                    if (logT.getRequestMethod().contains(CallSapApiServiceImpl.PRODUCTION_STORAGE_ROUTING)) {
                        //生产入库
                        ProductionStorageDto dto = JSON.parseObject(logT.getRequestParams(), ProductionStorageDto.class);
                        dto.getIS_BFLUSHDATAGEN().setPOSTDATE(DateUtil.dateToYyyyMmDd(new Date()));
                        productionStorage(dto);
                    } else if (logT.getRequestMethod().contains(CallSapApiServiceImpl.GOODS_MOVEMENT_ROUTING)) {
                        //货物移动 物料凭证创建
                        GoodsMovementDto dto = JSON.parseObject(logT.getRequestParams(), GoodsMovementDto.class);
                        dto.getHeader().setPSTNG_DATE(DateUtil.dateToYyyyMmDd(new Date()));
                        goodsMovement(dto);
                    } else if (logT.getRequestMethod().contains(CallSapApiServiceImpl.MOVE_WAREHOUSE_MONAD_ROUTING)) {
                        //移库单
                        List<MoveWarehouseMonadDto> dto = JSONObject.parseArray(logT.getRequestParams(), MoveWarehouseMonadDto.class);
                        for (MoveWarehouseMonadDto monadDto : dto) {
                            monadDto.setBUDAT(DateUtil.dateToYyyyMmDd(new Date()));
                        }
                        moveWarehouseMonad(dto);
                    } else if (logT.getRequestMethod().contains(CallSapApiServiceImpl.SALE_PLAY_AND_RETURN_ROUTING)) {
                        //销售发退货过账
                        List<SalePlayAndReturnDto> dto = JSONObject.parseArray(logT.getRequestParams(), SalePlayAndReturnDto.class);
                        salePlayAndReturn(dto);
                    }
                    warehouseMapper.deleteLogById(logT.getId());
                } catch (Exception e) {
                    return ResponseData.error(e.getMessage());
                }
            }

        }
        return ResponseData.success("success");
    }


    /**
     * 查询静默期如果已开启则不执行
     *
     * @param params
     * @param url
     * @param methodDesc
     */
    public boolean querySilentPeriodSet(String params, String url, String methodDesc, String code) {
        IapDictT dict = warehouseMapper.querySilentPeriodSet(code);
        if (null != dict && dict.getEnabled() == 0) {
            logUtil.log("SAP", params, url, "静默期开启延缓执行!", "0", methodDesc, "1");
            return true;
        }
        return false;
    }


}
