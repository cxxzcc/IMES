package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.client.service.MeProductInspectionItemsOrderNcCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author chenjx1
 * @date 2021/12/15
 */
@Component
@Slf4j
public class MeProductInspectionItemsOrderNcCodeServiceImpl implements MeProductInspectionItemsOrderNcCodeService {

    @Override
    public ResponseData<List<MeProductInspectionItemsOrderNcCode>> listItemNcCodes(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) {
        log.error("listItemNcCodes 调用失败");
        return ResponseData.error("listItemNcCodes 调用失败");
    }

    @Override
    public ResponseData info(String bo) {
        log.error("info 调用失败");
        return ResponseData.error("info 调用失败");
    }

    @Override
    public ResponseData<Collection<MeProductInspectionItemsOrderNcCode>> listByBos(List<String> bos) {
        log.error("listByBos 调用失败");
        return ResponseData.error("listByBos 调用失败");
    }

    @Override
    public ResponseData save(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException {
        log.error("save 调用失败");
        return ResponseData.error("save 调用失败");
    }

    @Override
    public ResponseData saveList(List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList) throws CommonException {
        log.error("saveList 调用失败");
        return ResponseData.error("saveList 调用失败");
    }

    @Override
    public ResponseData update(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) {
        log.error("update 调用失败");
        return ResponseData.error("update 调用失败");
    }

    @Override
    public ResponseData deleteByBos(String[] bos) {
        log.error("deleteByBos 调用失败");
        return ResponseData.error("deleteByBos 调用失败");
    }

    @Override
    public ResponseData deleteNcCodesByOrderBo(String orderBo) {
        log.error("deleteNcCodesByOrderBo 调用失败");
        return ResponseData.error("deleteNcCodesByOrderBo 调用失败");
    }

    @Override
    public ResponseData deleteByInspectionItemId(String orderBo, Integer inspectionItemId) {
        log.error("deleteByInspectionItemId 调用失败");
        return ResponseData.error("deleteByInspectionItemId 调用失败");
    }

    @Override
    public ResponseData deleteByInspectionItemIdItemType(String orderBo, Integer inspectionItemId, String itemType) {
        log.error("deleteByInspectionItemIdItemType 调用失败");
        return ResponseData.error("deleteByInspectionItemIdItemType 调用失败");
    }
}
