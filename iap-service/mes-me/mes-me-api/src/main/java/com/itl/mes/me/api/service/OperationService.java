package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.me.api.dto.OperationQueryDto;
import com.itl.mes.me.api.entity.Operation;

import java.util.List;
import java.util.Map;


/**
 * 操作
 *
 * @author yx
 * @date 2021-05-31
 */
public interface OperationService extends IService<Operation> {
    /**
     * 分页查询
     * @param queryDto
     * @return
     */
    IPage<Operation> queryPage(OperationQueryDto queryDto);

    /**
     * 保存
     * @param operation
     * @throws CommonException
     */
    void saveAndUpdate(Operation operation) throws CommonException;

    /**
     * 删除
     * @param bos
     */
    void delete(List<String> bos);

    /**
     * SN工序校验
     * @param sn
     * @param operationBo
     * @return
     * @throws CommonException
     */
    Map<String, Object> checkSnAndOperation(String sn, String operationBo) throws CommonException;

    /**
     * 条码绑定-获取装配清单
     * @param productSn 成品条码
     * @param traceMethod 追溯方式
     * @return
     * @throws CommonException
     */
    List<ShopOrderBomComponnetVo> getAssyList(String productSn, String traceMethod) throws CommonException;

    /**
     * 批次条码绑定-获取装配清单&上料清单
     * @param productSn 成品条码
     * @param operationBo 工序Bo
     * @return
     * @throws CommonException
     */
    Map<String, Object> getBatchAssyList(String productSn, String operationBo) throws CommonException;

    /**
     * 单体条码绑定
     * @param productSn 成品条码
     * @param childSn 子件条码
     * @return
     * @throws CommonException
     */
    List<ShopOrderBomComponnetVo> bindSingleSn(String productSn, String childSn) throws CommonException;

    /**
     * 保存临时信息
     * @param productSn 成品条码
     * @param list 信息
     */
    void saveAssyTempInfo(String productSn, List<ShopOrderBomComponnetVo> list);

    /**
     * 检验工单是否暂停/关闭
     * @param sn 条码
     * @return {@link ShopOrderFullVo} 工单entity
     * @throws CommonException
     */
    ShopOrderFullVo checkShopOrder(String sn) throws CommonException;

    /**
     * 批次条码绑定
     * @param productSn 成品条码
     * @param childSn 子件条码
     * @param operationBo 工序Bo
     * @return
     * @throws CommonException
     */
    Map<String, Object> bindBatchSn(String productSn, String childSn, String operationBo) throws CommonException;

    /**
     * 下料
     * @param ids 批次条码绑定临时上料表id
     * @return
     */
    Boolean unload(List<String> ids);

    /**
     * 更改sn_router表中的当前节点到下一节点, 包括null,
     * 更改me_product_status的下工序
     * @param sn 条码
     * @param operationBo 当前工位工序
     * @throws CommonException
     */
    void changeSnRouterCurrentNode(String sn, String result, String shopOrder, String operationBo) throws CommonException;

    /**
     * 条码当前工序与工位工序是否一致
     *
     * @param sn              条码
     * @param operationBo     当前登录工序
     * @param shopOrderFullVo 工单实体
     * @throws CommonException
     */
    void checkSnOperation(String sn, String operationBo, ShopOrderFullVo shopOrderFullVo) throws CommonException;
}

