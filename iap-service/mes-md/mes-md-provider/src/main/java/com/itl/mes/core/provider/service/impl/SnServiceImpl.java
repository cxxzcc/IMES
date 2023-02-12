package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.CarrierHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.SnService;
import com.itl.mes.core.provider.mapper.SnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 条码信息表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-25
 */
@Service
@Transactional
public class SnServiceImpl extends ServiceImpl<SnMapper, Sn> implements SnService {


    @Autowired
    private SnMapper snMapper;


    @Override
    public List<Sn> selectList() {
        QueryWrapper<Sn> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, sn);
        return super.list(entityWrapper);
    }

    @Override
    public Sn getExitsSn(SnHandleBO snHandleBO) throws CommonException {
        Sn snEntity = snMapper.selectById(snHandleBO.getBo());
        if (snEntity == null) {
            throw new CommonException("未找到SN:" + snHandleBO.getSn() + "条码信息，请查找原因", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return snEntity;
    }


    //获取自定义数据 值
    @Override
    public String getSelfDefiningData(String site, String customDataType, String field, String customDataValBo) {
        String vals = snMapper.getSelfDefiningData(site, customDataType, field, customDataValBo);
        return vals;
    }

    @Override
    public IPage selectPageSN(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = snMapper.selectPageSN(page, params);
        page.setRecords(mapList);
        return page;
    }

    @Override
    public List<Map<String, Object>> selectPageShopOrderByShape(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> maps = snMapper.selectPageShopOrderByShape(page, params);
        maps.forEach(x -> x.put("PLAN_END_DATE", Optional.ofNullable(x.get("PLAN_END_DATE")).map(y -> y.toString().split("\\.")[0]).orElse(null)));
        return maps;
    }




    @Override
    public Sn selectMaxSerial(String site, String complementCodeState, String ruleCode, String newDateSt) {
        return snMapper.selectMaxSerial(site, complementCodeState, ruleCode, newDateSt);
    }


    //修改工单排产数量
    @Override
    public void updateShopOrderSchedulQty(String shopOrderBO, int qty) {
        snMapper.updateShopOrderSchedulQty(shopOrderBO, qty);
    }

    //修改工单超产状态 及 超产数量 加一
    @Override
    public void updateShopOrderOverfulfillQty(String shopOrderBO, int qty) {
        snMapper.updateShopOrderOverfulfillQty(shopOrderBO, qty);
    }

    /**
     * 修改生产前工单返坯数
     *
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute2ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute2ByShopOrderBoAndQty(shopOrderBO, qty);
    }

    /**
     * 修改生产前工单破损数
     *
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute3ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute3ByShopOrderBoAndQty(shopOrderBO, qty);
    }

    /**
     * 修改生产后工单返坯数
     *
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute4ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute4ByShopOrderBoAndQty(shopOrderBO, qty);
    }

    /**
     * 修改生产后工单破损数
     *
     * @param shopOrderBO
     * @param qty
     */
    @Override
    public void updateAttribute5ByShopOrderBoAndQty(String shopOrderBO, int qty) {
        snMapper.updateAttribute5ByShopOrderBoAndQty(shopOrderBO, qty);
    }

    /**
     * 工单拆单-变更条码工单
     * @param snBoList
     * @param newOrderBo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData updateOrderBo(List<String> snBoList, String newOrderBo) {
        final Sn sn1 = new Sn();
        sn1.setShopOrder(newOrderBo);
        snMapper.update(sn1, new QueryWrapper<Sn>().lambda().in(Sn::getBo, snBoList));
        return ResponseData.success();
    }

    @Override
    /**
     * 拆单使用：查询拆当前工单产品SN，未上线状态的条码BO
     * @param orderBo
     * @param onLine
     * @return
     */
    public List<String> queryOrderBoList(String orderBo,int onLine) {
        List<String> boList = new ArrayList<>();
        snMapper.queryOrderBoList(orderBo,onLine);
        return boList;
    }

    @Override
    public Boolean checkExistsBySn(String sn, String site) {
        return lambdaQuery().eq(Sn::getSite, site).eq(Sn::getSn, sn).count() > 0;
    }
}
