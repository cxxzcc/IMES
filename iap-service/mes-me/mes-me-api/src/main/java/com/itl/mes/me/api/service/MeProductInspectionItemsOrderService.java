package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;

import java.util.List;

/**
 * 产品检验项目-工单副本
 *
 * @author chenjx1
 * @date 2021-10-20
 */
public interface MeProductInspectionItemsOrderService extends IService<MeProductInspectionItemsOrderEntity> {

    /**
     * 保存
     */
    boolean save(MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity) throws CommonException;

    /**
     * 保存集合
     */
    boolean saveList(List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList) throws CommonException;

    /**
     * 删除工单检验项目副本
     */
    int deleteOrderItems(String orderBo) throws CommonException;

}

