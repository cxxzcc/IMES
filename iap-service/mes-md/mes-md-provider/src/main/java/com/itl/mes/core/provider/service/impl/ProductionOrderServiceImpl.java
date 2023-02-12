package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.common.base.constants.SystemDictCodeConstant;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.ProductionOrderSourceEnum;
import com.itl.mes.core.api.constant.ProductionOrderStatusEnum;
import com.itl.mes.core.api.entity.ProductionOrder;
import com.itl.mes.core.api.service.ProductionOrderService;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.provider.feign.CustomerFeignService;
import com.itl.mes.core.provider.mapper.ProductionOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生产订单服务实现类
 * @author dengou
 * @date 2021/10/11
 */
@Service
public class ProductionOrderServiceImpl  extends ServiceImpl<ProductionOrderMapper, ProductionOrder>  implements ProductionOrderService {



    @Autowired
    private CodeRuleService codeRuleService;
    @Autowired
    private CustomerFeignService customerFeignService;
    @Autowired
    private DictService dictService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrder(ProductionOrder productionOrder) {
        String id = productionOrder.getId();
        if(StrUtil.isBlank(id)) {
            return addOrder(productionOrder);
        } else {
            return updateOrder(productionOrder);
        }
    }

    /**
     * 新增订单
     * */
    private Boolean addOrder(ProductionOrder productionOrder) {
        String orderNo = productionOrder.getOrderNo();
        if(StrUtil.isBlank(orderNo)) {
            ResponseData<String> orderNoResult = codeRuleService.generatorNextNumber("SCDDBH");
            Assert.valid(!orderNoResult.isSuccess(), "生成订单编号失败");
            productionOrder.setOrderNo(orderNoResult.getData());
        } else {
            //校验订单编号是否重复
            Assert.valid(checkOrderNoExists(orderNo), "订单编号已存在");
        }
        productionOrder.setSite(UserUtils.getSite());
        productionOrder.setStatus(ProductionOrderStatusEnum.NOT_ASSIGN.getCode());
        productionOrder.setCompleteQuantity(0);
        productionOrder.setScheduledQuantity(0);
        productionOrder.setCreateBy(UserUtils.getUserId());
        productionOrder.setCreateDate(new Date());
        return save(productionOrder);
    }

    /**
     * 更新订单
     * */
    private Boolean updateOrder(ProductionOrder productionOrder) {

        ProductionOrder queryById = getById(productionOrder.getId());
        Assert.valid(queryById == null, "生产订单不存在");
        List<String> notDeleteList = ListUtil.toList(ProductionOrderStatusEnum.NOT_ASSIGN.getCode());
        Assert.valid(!CollUtil.contains(notDeleteList, queryById.getStatus()), "该订单不可编辑");

        //订单编号如果一致， 不修改，  不一致则需要检查是否存在
        if(StrUtil.equals(productionOrder.getOrderNo(), queryById.getOrderNo())) {
            productionOrder.setOrderNo(null);
        } else {
            //校验订单编号是否重复
            Assert.valid(checkOrderNoExists(productionOrder.getOrderNo()), "订单编号已存在");
        }
        productionOrder.setUpdateBy(UserUtils.getUserId());
        productionOrder.setUpdateDate(new Date());
        return updateById(productionOrder);
    }

    /**
     * 校验订单编号是否已存在
     * @param orderNo 订单编号
     * @return 是否已存在
     * */
    private Boolean checkOrderNoExists(String orderNo) {
        Integer count = lambdaQuery().eq(ProductionOrder::getSite, UserUtils.getSite()).eq(ProductionOrder::getOrderNo, orderNo).count();
        return count > 0;
    }

    @Override
    public ProductionOrder detailById(String id) {
        ProductionOrder productionOrder = getById(id);
        if(productionOrder == null) {
            return null;
        }
        ResponseData<Map<String, String>> dictItemMapByParentCode = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.PRODUCTION_ORDER_TYPE);
        if(dictItemMapByParentCode.isSuccess()) {
            Map<String, String> data = dictItemMapByParentCode.getData();
            productionOrder.setTypeDesc(data.getOrDefault(productionOrder.getType(), null));
        }
        productionOrder.setSourceDesc(ProductionOrderSourceEnum.parseDescByCode(productionOrder.getSource()));
        productionOrder.setStatusDesc(ProductionOrderStatusEnum.parseDescByCode(productionOrder.getStatus()));
        ResponseData<List<Map<String, Object>>> response = customerFeignService.getByIds(Arrays.asList(productionOrder.getCustomId()));
        if(response.isSuccess()) {
            List<Map<String, Object>> data = response.getData();
            Map<String, String> map = data.stream().collect(Collectors.toMap(e -> (String) e.get("id"), e -> (String) e.get("customerName")));
            if(map.containsKey(productionOrder.getCustomId())) {
                productionOrder.setCustomName(map.get(productionOrder.getCustomId()));
            }
        }
        return productionOrder;
    }

    @Override
    public ProductionOrder detailByOrderNo(String orderNo) {
        ProductionOrder productionOrder = lambdaQuery().eq(ProductionOrder::getOrderNo, orderNo).one();
        if(productionOrder == null) {
            return null;
        }
        ResponseData<Map<String, String>> dictItemMapByParentCode = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.PRODUCTION_ORDER_TYPE);
        if(dictItemMapByParentCode.isSuccess()) {
            Map<String, String> data = dictItemMapByParentCode.getData();
            productionOrder.setTypeDesc(data.getOrDefault(productionOrder.getType(), null));
        }
        productionOrder.setSourceDesc(ProductionOrderSourceEnum.parseDescByCode(productionOrder.getSource()));
        productionOrder.setStatusDesc(ProductionOrderStatusEnum.parseDescByCode(productionOrder.getStatus()));
        ResponseData<List<Map<String, Object>>> response = customerFeignService.getByIds(Arrays.asList(productionOrder.getCustomId()));
        if(response.isSuccess()) {
            List<Map<String, Object>> data = response.getData();
            Map<String, String> map = data.stream().collect(Collectors.toMap(e -> (String) e.get("id"), e -> (String) e.get("customerName")));
            if(map.containsKey(productionOrder.getCustomId())) {
                productionOrder.setCustomName(map.get(productionOrder.getCustomId()));
            }
        }
        return productionOrder;
    }

    @Override
    public Page<ProductionOrder> queryPage(Map<String, Object> params) {
        Page<ProductionOrder> page = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        List<ProductionOrder> list= baseMapper.getPage(page, params);
        if(CollUtil.isNotEmpty(list)) {
            List<String> ids = list.stream().map(ProductionOrder::getCustomId).collect(Collectors.toList());
            ResponseData<List<Map<String, Object>>> response = customerFeignService.getByIds(ids);
            Map<String, String> map = new HashMap<>();
            if(response.isSuccess()) {
                List<Map<String, Object>> data = response.getData();
                map = data.stream().collect(Collectors.toMap(e -> (String) e.get("id"), e -> (String) e.get("customerName")));
            }
            Map<String, String> finalMap = map;
            ResponseData<Map<String, String>> dictItemMapByParentCode = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.PRODUCTION_ORDER_TYPE);
            Map<String, String> data = new HashMap<>(16);
            if(dictItemMapByParentCode.isSuccess()) {
                Map<String, String> dictMap = dictItemMapByParentCode.getData();
                if(CollUtil.isNotEmpty(dictMap)) {
                    data.putAll(dictMap);
                }
            }
            list.forEach(e -> {
                e.setTypeDesc(data.getOrDefault(e.getType(),  null));
                e.setSourceDesc(ProductionOrderSourceEnum.parseDescByCode(e.getSource()));
                e.setStatusDesc(ProductionOrderStatusEnum.parseDescByCode(e.getStatus()));
                if(finalMap.containsKey(e.getCustomId())) {
                    e.setCustomName(finalMap.get(e.getCustomId()));
                }
            });
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Boolean deleteById(String id) {
        ProductionOrder productionOrder = getById(id);
        Assert.valid(productionOrder == null, "生产订单不存在");
        List<String> notDeleteList = ListUtil.toList(ProductionOrderStatusEnum.NOT_ASSIGN.getCode());
        Assert.valid(!CollUtil.contains(notDeleteList, productionOrder.getStatus()), "该订单不可删除");
        return baseMapper.deleteById(id) > 0;
    }

}
