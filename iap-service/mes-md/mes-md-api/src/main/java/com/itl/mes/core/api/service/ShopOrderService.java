package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.ShopOrderFullVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
public interface ShopOrderService extends IService<ShopOrder> {

    List<ShopOrder> selectList();

    /**
     * 通过ShopOrderHandleBO查询存在的工单
     *
     * @param shopOrderHandleBO
     * @return
     * @throws CommonException
     */
    ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO)throws CommonException;

    /**
     * 通过ShopOrderHandleBO查询工单
     *
     * @param shopOrderHandleBO
     * @return
     */
    ShopOrder getShopOrder(ShopOrderHandleBO shopOrderHandleBO);

    /**
     * 查询工单相关数据
     *
     * @param shopOrderHandleBO
     * @return
     * @throws CommonException
     */
    ShopOrderFullVo getShopFullOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException;

    /**
     * 保存工单相关数据
     *
     * @param shopOrderFullVo
     * @return
     * @throws CommonException
     */
    void saveShopOrder(ShopOrderFullVo shopOrderFullVo) throws CommonException;

    /**
     * 暂停状态
     *
     * @param shopOrderFullVo
     * @return
     * @throws CommonException
     */
    Boolean stopByStatus (ShopOrderFullVo shopOrderFullVo) throws CommonException;

    /**
     * 恢复状态
     *
     * @param shopOrderFullVo
     * @return
     * @throws CommonException
     */
    Boolean recoveryByStatus (ShopOrderFullVo shopOrderFullVo) throws CommonException;

    /**
     * 关闭状态
     *
     * @param shopOrderFullVo
     * @return
     * @throws CommonException
     */
    Boolean closeByStatus(ShopOrderFullVo shopOrderFullVo) throws CommonException;

    /**
     * 删除工单数据
     *
     * @param shopOrderHandleBO
     * @param modifyDate
     */
    void deleteShopOrderByHandleBO(ShopOrderHandleBO shopOrderHandleBO, Date modifyDate) throws CommonException;


    //工单下达
    //List<Sfc> shopOrderRelease(String shopOrder, BigDecimal stayNumber, String planStartDate, String planEndDate) throws BusinessException;

    List<ShopOrder> getShopOrderByBomBO(String bomBO);

    //根据Sfc和数量工单下达
    //List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate, List<SfcAndNumVo> sfcAndNumVos) throws BusinessException;

    //List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate,String routerBo, List<SfcAndNumVo> sfcAndNumVos) throws BusinessException;

    /**
     * 更新工单完成数量
     *
     * @param bo 工单BO
     * @param completeQty 完成数量
     * @return Integer
     */
    Integer updateShopOrderCompleteQtyByBO(String bo, BigDecimal completeQty);

    /**
     * 更新工单报废数量
     *
     * @param bo 工单BO
     * @param scrapTty 报废数量
     * @return Integer
     */
    Integer updateShopOrderScrapQtyByBO(String bo, BigDecimal scrapTty);

    /**
     * 更新工单标签数量
     *
     * @param bo 工单BO
     * @param labelQty 标签数量
     * @return Integer
     */
    Integer updateShopOrderLabelQtyByBO(String bo, BigDecimal labelQty);

    /**
     * 更新工单排产数量
     *
     * @param bo 工单BO
     * @param scheduleQty 排产数量
     * @return Integer
     */
    Integer updateShopOrderScheduleQtyByBO(String bo, BigDecimal scheduleQty);

    /**
     * 更新工单排产、工单数量
     *
     * @param bo 工单BO
     * @param scheduleQty 排产数量
     * @return Integer
     */
    Integer updateShopOrderScheduleQtyAndOrderQtyByBO(String bo, BigDecimal scheduleQty, BigDecimal orderQty);


    Object getAllOrder(Map<String,Object> params, Integer page, Integer pageSize);

    void updateShopOrderFullVo(ShopOrderFullVo shopOrderFullVo);

    void updateEmergenc(List<Map<String, Object>> shopOrderList);

    void updateFixedTime(String shopOrder, String fixedTime);

    List<MboMitemDTO> getBomComponents(String shopOrderBo);

    /**
     * 更新工单完工数量和是否完工状态
     * 工单完工数量增加 {{completeQty}} , 并判断增加之后的工单完工数量是否等于工单数量， 等于则更新工单状态为完工
     * @param shopOrderBo 工单bo
     * @param completeQty 完工数量
     * */
    Boolean updateShopOrderCompleteQtyAndState(String shopOrderBo, Integer completeQty);


    /**
     * 根据SFC主键修改目标工单的完成数
     *
     * @param targetShopOrderBo 目标工单
     * @param sfcBo sfc主键
     * @throws BusinessException 异常
     */
    //void changeShopOrderCompleteQty(String targetShopOrderBo, String sfcBo) throws BusinessException;
}
