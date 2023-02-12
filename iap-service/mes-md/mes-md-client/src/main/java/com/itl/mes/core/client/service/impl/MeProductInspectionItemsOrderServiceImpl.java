package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.client.service.MeProductInspectionItemsOrderService;
import com.itl.mes.me.api.dto.MeProductGroupInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsOrderDto;
import com.itl.mes.me.api.entity.MeProductGroupInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author chenjx1
 * @date 2021/10/21
 */
@Component
@Slf4j
public class MeProductInspectionItemsOrderServiceImpl implements MeProductInspectionItemsOrderService {

    @Override
    public ResponseData listItemsOrder(MeProductInspectionItemsOrderDto meProductInspectionItemsOrderDto) {
        return null;
    }

    @Override
    public List<MeProductInspectionItemsEntity> listItems(MeProductInspectionItemsDto meProductInspectionItemsDto) {
        return null;
    }

    @Override
    public List<MeProductGroupInspectionItemsEntity> listGroupItems(MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto) {
        return null;
    }

    @Override
    public ResponseData info(Integer id) {
        return null;
    }

    @Override
    public ResponseData<Collection<MeProductInspectionItemsOrderEntity>> listByIds(List<Integer> ids) {
        log.error("listByIds 调用失败");
        return ResponseData.error("listByIds 调用失败");
    }

    @Override
    public ResponseData save(MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity) throws CommonException {
        return null;
    }

    @Override
    public ResponseData saveList(List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList) throws CommonException {
        return null;
    }

    @Override
    public ResponseData update(MeProductInspectionItemsOrderEntity meProductInspectionItemsOrder) {
        return null;
    }

    @Override
    public ResponseData delete(Integer[] ids) {
        return null;
    }

    @Override
    public ResponseData deleteOrderItems(String orderBo) {
        return ResponseData.error("更新时，删除工单检验项目失败！");
    }

}
