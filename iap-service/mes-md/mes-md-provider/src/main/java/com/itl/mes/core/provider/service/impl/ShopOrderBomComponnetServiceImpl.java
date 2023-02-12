package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetQueryDto;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetSaveDto;
import com.itl.mes.core.api.entity.BomComponnet;
import com.itl.mes.core.api.entity.ShopOrderBomComponnet;
import com.itl.mes.core.api.service.ShopOrderBomComponnetService;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.core.provider.mapper.BomComponnetMapper;
import com.itl.mes.core.provider.mapper.ShopOrderBomComponnetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auth liuchenghao
 * @date 2021/5/27
 */
@Service
public class ShopOrderBomComponnetServiceImpl extends ServiceImpl<ShopOrderBomComponnetMapper, ShopOrderBomComponnet> implements ShopOrderBomComponnetService {

    @Autowired
    ShopOrderBomComponnetMapper shopOrderBomComponnetMapper;

    @Autowired
    BomComponnetMapper bomComponnetMapper;

    @Override
    public IPage<ShopOrderBomComponnetVo> findList(ShopOrderBomComponnetQueryDto shopOrderBomComponnetQueryDto) {

        if (ObjectUtil.isEmpty(shopOrderBomComponnetQueryDto.getPage())) {
            shopOrderBomComponnetQueryDto.setPage(new Page(0, 10));
        }
        IPage<ShopOrderBomComponnetVo> shopOrderBomComponnetVoIPage = null;
        shopOrderBomComponnetQueryDto.setSite(UserUtils.getSite());
        if (StrUtil.isNotBlank(shopOrderBomComponnetQueryDto.getBomBo())) {
            shopOrderBomComponnetVoIPage = shopOrderBomComponnetMapper.findListByBomBo(shopOrderBomComponnetQueryDto.getPage(), shopOrderBomComponnetQueryDto);
        }
        if (StrUtil.isNotBlank(shopOrderBomComponnetQueryDto.getShopOrderBo())) {
            shopOrderBomComponnetVoIPage = shopOrderBomComponnetMapper.findListByShopOrderBo(shopOrderBomComponnetQueryDto.getPage(), shopOrderBomComponnetQueryDto);
        }

        return shopOrderBomComponnetVoIPage;
    }

    @Override
    public List<ShopOrderBomComponnetVo> queryBomByShopOrderBo(String shopOrderBo, String type) {
        return shopOrderBomComponnetMapper.queryBomByShopOrderBo(shopOrderBo, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto) throws CommonException {

        //当还未新增工单时，根据工单的BOM查询出组件信息，然后再进行批量插入数据
        if (StrUtil.isNotBlank(shopOrderBomComponnetSaveDto.getBomBo())) {
            UpdateWrapper<ShopOrderBomComponnet> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("SHOP_ORDER_BO", shopOrderBomComponnetSaveDto.getShopOrderBo());
            updateWrapper.eq("type", shopOrderBomComponnetSaveDto.getType());
            shopOrderBomComponnetMapper.delete(updateWrapper);

            QueryWrapper<BomComponnet> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(BomComponnet.BOM_BO, shopOrderBomComponnetSaveDto.getBomBo());
            List<BomComponnet> bomComponnets = bomComponnetMapper.selectList(queryWrapper);

            if (CollUtil.isNotEmpty(bomComponnets)) {
                bomComponnets.forEach(bomComponnet -> {
                    ShopOrderBomComponnet shopOrderBomComponnet = new ShopOrderBomComponnet();
                    BeanUtil.copyProperties(bomComponnet, shopOrderBomComponnet);
                    shopOrderBomComponnet.setBo(null);
                    shopOrderBomComponnet.setShopOrderBo(shopOrderBomComponnetSaveDto.getShopOrderBo());
                    shopOrderBomComponnet.setType(shopOrderBomComponnetSaveDto.getType());
                    shopOrderBomComponnet.setSite(UserUtils.getSite());
                    shopOrderBomComponnetMapper.insert(shopOrderBomComponnet);
                });

            }
        } else {
            if (CollUtil.isNotEmpty(shopOrderBomComponnetSaveDto.getShopOrderBomComponnets())) {
                /*if (shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().stream().map(ShopOrderBomComponnet::getComponentBo).distinct().count() < shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().size()) {
                    throw new CommonException("检测到相同物料出现在多行!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }*/

                // 工单BOM\工序BOM校验
                String site = UserUtils.getSite();
                String shopOrderBo = shopOrderBomComponnetSaveDto.getShopOrderBo();
                String type = shopOrderBomComponnetSaveDto.getType();
                if(shopOrderBo == null || "".equals(shopOrderBo)){
                    throw new CommonException("shopOrderBo参数的值不能为空!", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else if(type == null || "".equals(type)){
                    throw new CommonException("type参数的值不能为空!", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }

                // 查找不同的装配工序
                List<String> collect1 = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().stream().map(ShopOrderBomComponnet::getOperationBo).distinct().collect(Collectors.toList());
                for(int i=0;i < collect1.size();i++){
                    String operationBoStr = collect1.get(i);
                    if(operationBoStr != null){
                        // 查找同一装配工序，同组件
                        //long countNum = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().stream().filter(shopOrderBomComponnet -> operationBoStr.equals(shopOrderBomComponnet.getOperationBo())).map(ShopOrderBomComponnet::getComponentBo).distinct().count();
                        // 查找同一装配工序，不同组件
                        // Stream<String> stringStream = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().stream().filter(shopOrderBomComponnet -> operationBoStr.equals(shopOrderBomComponnet.getOperationBo())).map(ShopOrderBomComponnet::getComponentBo);
                        // 同一装配工序，全部组件数
                        long countAll = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().stream().filter(shopOrderBomComponnet -> operationBoStr.equals(shopOrderBomComponnet.getOperationBo())).map(ShopOrderBomComponnet::getComponentBo).count();
                        // 同一装配工序，去重后组件数
                        long countDistinct = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().stream().filter(shopOrderBomComponnet -> operationBoStr.equals(shopOrderBomComponnet.getOperationBo())).map(ShopOrderBomComponnet::getComponentBo).distinct().count();
                        if(countAll > countDistinct){
                            if(type == "SO" || "SO".equals(type)){
                                throw new CommonException("工单BOM检测到相同工序且物料相同的数据出现多行!", CommonExceptionDefinition.BASIC_EXCEPTION);
                            }else if(type == "OP" || "OP".equals(type)){
                                throw new CommonException("工序BOM检测到相同工序且物料相同的数据出现多行!", CommonExceptionDefinition.BASIC_EXCEPTION);
                            }
                            throw new CommonException("检测到相同工序且物料相同的数据出现多行!", CommonExceptionDefinition.BASIC_EXCEPTION);
                        }
                    }

                }

                // 方式1：删除
                UpdateWrapper<ShopOrderBomComponnet> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("SHOP_ORDER_BO", shopOrderBo);
                updateWrapper.eq("type", type);
                shopOrderBomComponnetMapper.delete(updateWrapper);

                // 方式1：新增
                shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().forEach(shopOrderBomComponnet -> {
                    shopOrderBomComponnet.setBo(null);
                    shopOrderBomComponnet.setShopOrderBo(shopOrderBo);
                    shopOrderBomComponnet.setQty(shopOrderBomComponnet.getQty() == null ? BigDecimal.ZERO : shopOrderBomComponnet.getQty());
                    shopOrderBomComponnet.setSite(site);
                    shopOrderBomComponnet.setType(type);
                    shopOrderBomComponnetMapper.insert(shopOrderBomComponnet);
                });

                // 方式2：新增、修改
//                shopOrderBomComponnetSaveDto.getShopOrderBomComponnets().forEach(shopOrderBomComponnet -> {
//                    if (StrUtil.isNotBlank(shopOrderBomComponnet.getBo())) {
//                        shopOrderBomComponnetMapper.updateById(shopOrderBomComponnet);
//                    } else {
//                        shopOrderBomComponnet.setBo(null);
//                        shopOrderBomComponnet.setShopOrderBo(shopOrderBo);
//                        shopOrderBomComponnet.setQty(shopOrderBomComponnet.getQty() == null ? BigDecimal.ZERO : shopOrderBomComponnet.getQty());
//                        shopOrderBomComponnet.setSite(site);
//                        shopOrderBomComponnet.setType(type);
//                        shopOrderBomComponnetMapper.insert(shopOrderBomComponnet);
//                    }
//                });
            }

        }

    }


}
