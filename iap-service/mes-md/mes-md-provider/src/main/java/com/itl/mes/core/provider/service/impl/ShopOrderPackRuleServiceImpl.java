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
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.dto.ShopOrderPackRuleQueryDto;
import com.itl.mes.core.api.dto.ShopOrderPackRuleSaveDto;
import com.itl.mes.core.api.entity.ItemPackRuleDetail;
import com.itl.mes.core.api.entity.ShopOrderPackRuleDetail;
import com.itl.mes.core.api.service.ShopOrderPackRuleService;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import com.itl.mes.core.api.vo.ShopOrderPackRuleVo;
import com.itl.mes.core.provider.mapper.ItemPackRuleDetailMapper;
import com.itl.mes.core.provider.mapper.PackingRuleMapper;
import com.itl.mes.core.provider.mapper.ShopOrderPackRuleDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 */
@Service
public class ShopOrderPackRuleServiceImpl extends ServiceImpl<ShopOrderPackRuleDetailMapper, ShopOrderPackRuleDetail> implements ShopOrderPackRuleService {


    @Autowired
    PackingRuleMapper packingRuleMapper;

    @Autowired
    ItemPackRuleDetailMapper itemPackRuleDetailMapper ;

    @Autowired
    ShopOrderPackRuleDetailMapper shopOrderPackRuleDetailMapper;


    @Override
    public ShopOrderPackRuleVo findList(ShopOrderPackRuleQueryDto shopOrderPackRuleQueryDto) {

        if (ObjectUtil.isEmpty(shopOrderPackRuleQueryDto.getPage())) {
            shopOrderPackRuleQueryDto.setPage(new Page(0, 10));
        }

        ShopOrderPackRuleVo shopOrderPackRuleVo = new ShopOrderPackRuleVo();
        IPage<ShopOrderPackRuleDetailVo> packRuleDetailVoIPage;
        if (StrUtil.isNotBlank(shopOrderPackRuleQueryDto.getShopOrderBo())) {
            packRuleDetailVoIPage = shopOrderPackRuleDetailMapper.findListByShopOrderBo(shopOrderPackRuleQueryDto.getPage(), shopOrderPackRuleQueryDto);
        } else {
            packRuleDetailVoIPage = shopOrderPackRuleDetailMapper.findListByRuleBo(shopOrderPackRuleQueryDto.getPage(), shopOrderPackRuleQueryDto);

        }

        if (CollUtil.isNotEmpty(packRuleDetailVoIPage.getRecords())) {
            shopOrderPackRuleVo.setPackRuleBo(packRuleDetailVoIPage.getRecords().get(0).getPackRuleBo());
            shopOrderPackRuleVo.setPackRule(packRuleDetailVoIPage.getRecords().get(0).getPackRule());

            shopOrderPackRuleVo.setShopOrderPackRuleDetailVos(packRuleDetailVoIPage);
        }

        return shopOrderPackRuleVo;
    }

    @Override
    public void save(ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto) {

        Date date = new Date();


        if (StrUtil.isNotBlank(shopOrderPackRuleSaveDto.getItemBo())) {

            UpdateWrapper<ShopOrderPackRuleDetail> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("SHOP_ORDER_BO", shopOrderPackRuleSaveDto.getShopOrderBo());
            shopOrderPackRuleDetailMapper.delete(updateWrapper);
            QueryWrapper<ItemPackRuleDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_bo", shopOrderPackRuleSaveDto.getItemBo());
            List<ItemPackRuleDetail> itemPackRuleDetails = itemPackRuleDetailMapper.selectList(queryWrapper);

            if (CollUtil.isNotEmpty(itemPackRuleDetails)) {
                itemPackRuleDetails.forEach(itemPackRuleDetail -> {
                    ShopOrderPackRuleDetail shopOrderPackRuleDetail = new ShopOrderPackRuleDetail();
                    BeanUtil.copyProperties(itemPackRuleDetail, shopOrderPackRuleDetail);
                    shopOrderPackRuleDetail.setBo(null);
                    shopOrderPackRuleDetail.setShopOrderBo(shopOrderPackRuleSaveDto.getShopOrderBo());
                    shopOrderPackRuleDetail.setCreateUser(UserUtils.getCurrentUser().getUserName());
                    shopOrderPackRuleDetail.setCreateDate(date);
                    shopOrderPackRuleDetail.setModifyDate(null);
                    shopOrderPackRuleDetail.setModifyUser(null);
                    shopOrderPackRuleDetailMapper.insert(shopOrderPackRuleDetail);
                });

            }

        } else {
            UpdateWrapper<ShopOrderPackRuleDetail> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("SHOP_ORDER_BO", shopOrderPackRuleSaveDto.getShopOrderBo());
            shopOrderPackRuleDetailMapper.delete(updateWrapper);

            shopOrderPackRuleSaveDto.getShopOrderPackRuleDetails().forEach(shopOrderPackRuleDetail -> {
                shopOrderPackRuleDetail.setPackRuleBo(shopOrderPackRuleSaveDto.getPackRuleBo());
                shopOrderPackRuleDetail.setShopOrderBo(shopOrderPackRuleSaveDto.getShopOrderBo());
                shopOrderPackRuleDetail.setBo(null);
                shopOrderPackRuleDetail.setCreateDate(date);
                shopOrderPackRuleDetail.setCreateUser(UserUtils.getCurrentUser().getUserName());
                shopOrderPackRuleDetailMapper.insert(shopOrderPackRuleDetail);
                /*if (StrUtil.isNotBlank(shopOrderPackRuleDetail.getBo())) {
                    shopOrderPackRuleDetail.setShopOrderBo(shopOrderPackRuleSaveDto.getShopOrderBo());
                    shopOrderPackRuleDetail.setModifyUser(UserUtils.getCurrentUser().getUserName());
                    shopOrderPackRuleDetail.setModifyDate(date);
                    shopOrderPackRuleDetailMapper.updateById(shopOrderPackRuleDetail);
                } else {
                    shopOrderPackRuleDetail.setBo(null);
                    shopOrderPackRuleDetail.setShopOrderBo(shopOrderPackRuleSaveDto.getShopOrderBo());
                    shopOrderPackRuleDetail.setCreateDate(date);
                    shopOrderPackRuleDetail.setCreateUser(UserUtils.getCurrentUser().getUserName());
                    shopOrderPackRuleDetailMapper.insert(shopOrderPackRuleDetail);
                }*/
            });
        }

    }


    @Override
    public List<ShopOrderPackRuleDetailVo> listPackRuleDetailByShopOrderBo(String shopOrderBo) {
        List<ShopOrderPackRuleDetail> list = lambdaQuery().eq(ShopOrderPackRuleDetail::getShopOrderBo, shopOrderBo).list();
        if (list == null || list.size() == 0) {
            return Collections.emptyList();
        }
        return list.stream().map(x -> {
            ShopOrderPackRuleDetailVo t = new ShopOrderPackRuleDetailVo();
            t.setBo(x.getBo());
            t.setPackRuleBo(x.getPackRuleBo());
            t.setPackName(x.getPackName());
            t.setPackLevel(x.getPackLevel());
            t.setRuleMould(x.getRuleMouldBo());
            t.setMaxQty(x.getMaxQty());
            t.setMinQty(x.getMinQty());
            t.setRuleMouldBo(x.getRuleMouldBo());
            return t;
        }).collect(Collectors.toList());
    }
}
