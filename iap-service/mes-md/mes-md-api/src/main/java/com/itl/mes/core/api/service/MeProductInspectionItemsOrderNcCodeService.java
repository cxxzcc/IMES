package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;

import java.util.List;

/**
 * 产品检验项目-不良代码-工单副本
 *
 * @author chenjx1
 * @date 2021-12-10
 */
public interface MeProductInspectionItemsOrderNcCodeService extends IService<MeProductInspectionItemsOrderNcCode> {

    /**
     * 数据校验
     */
    boolean checkItemsNcCode(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException;

    /**
     * 保存
     */
    boolean save(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException;

    /**
     * 保存集合
     */
    boolean saveList(List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList) throws CommonException;

    /**
     * 根据工单BO删除不良代码
     */
    int deleteNcCodesByOrderBo(String orderBo) throws CommonException;

    /**
     * 根据工单BO+产品检验项目ID删除不良代码
     */
    int deleteByInspectionItemId(String orderBo, Integer inspectionItemId) throws CommonException;

    /**
     * 根据工单BO+产品检验项目ID+产品类型删除不良代码
     */
    int deleteByInspectionItemIdItemType(String orderBo, Integer inspectionItemId, String itemType) throws CommonException;

    /**
     * 根据工单bo和检验项目id查询不合格代码列表
     * @param orderBo 工单bo
     * @param inspectionItemId 检验项目id
     * @return 不合格代码列表
     * */
    List<MeProductInspectionItemsOrderNcCode> listByOrderAndInspection(String orderBo, Integer inspectionItemId);

}

