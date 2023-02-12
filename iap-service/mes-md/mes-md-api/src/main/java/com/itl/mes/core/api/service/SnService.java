package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.entity.Sn;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 条码信息表 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-25
 */
public interface SnService extends IService<Sn> {
    List<Sn> selectList();

    Sn getExitsSn(SnHandleBO snHandleBO) throws CommonException;

    String getSelfDefiningData(String site, String customDataType, String field, String customDataValBo );

    IPage selectPageSN(IPage<Map<String, Object>> page, Map<String, Object> params);

    List<Map<String,Object>> selectPageShopOrderByShape(IPage<Map<String,Object>> page, Map<String, Object> params);

    //查询产品生产数据
    //SnDataVo selectSnAndUserPassLog(String sn) throws BusinessException;

    Sn selectMaxSerial(String site, String complementCodeState, String ruleCode, String newDateStr);

    void updateShopOrderSchedulQty(String shopOrderBO, int qty);

    void updateShopOrderOverfulfillQty(String shopOrderBO, int qty);

    /**
     * 修改生产前工单返坯数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute2ByShopOrderBoAndQty(String shopOrderBO, int qty);

    /**
     * 修改生产前工单破损数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute3ByShopOrderBoAndQty(String shopOrderBO, int qty);

    /**
     * 修改生产后工单返坯数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute4ByShopOrderBoAndQty(String shopOrderBO, int qty);

    /**
     * 修改生产后工单破损数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute5ByShopOrderBoAndQty(String shopOrderBO, int qty);

    /**
     * 工单拆单-变更条码工单
     * @param snBoList
     * @param newOrderBo
     * @return
     */
    ResponseData updateOrderBo(List<String> snBoList, String newOrderBo);

    /**
     * 拆单使用：查询拆当前工单产品SN，未上线状态的条码BO
     * @param orderBo
     * @param onLine
     * @return
     */
    List<String> queryOrderBoList(String orderBo,int onLine);

    /**
     * 校验是否存在
     * @param sn 条码
     * @param site 工厂
     * @return 是否存在
     * */
    Boolean checkExistsBySn(String sn, String site);


}
