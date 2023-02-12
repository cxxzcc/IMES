package com.itl.mom.label.provider.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.entity.label.SnItem;
import com.itl.mom.label.api.service.SnItemService;
import com.itl.mom.label.provider.mapper.label.SnItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GKL
 * @date 2021/11/10 - 15:47
 * @since 2021/11/10 - 15:47 星期三 by GKL
 */
@Service
@Slf4j
public class SnItemServiceImpl extends ServiceImpl<SnItemMapper, SnItem> implements SnItemService {


    @Override
    public ResponseData<SnItem> querySnItemByItemBoAndSnBo(String itemBo, String snBo) {


        //校验物料编码是否在物料bom中  SnItem  snItemMapper
        LambdaQueryWrapper<SnItem> snItemLambdaQueryWrapper = new QueryWrapper<SnItem>().lambda()
                .eq(SnItem::getItemBo, itemBo)
                .eq(SnItem::getSnBo, snBo);
        SnItem snItem = baseMapper.selectOne(snItemLambdaQueryWrapper);
        return ResponseData.success(snItem);
    }

    /**
     * 通过id修改剩余数量
     *
     * @param id id
     * @return ResponseData.class
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public ResponseData<Boolean> updateSysl(String id, BigDecimal sysl) {
        int i = baseMapper.updateSysl(id, sysl);
        if (i <= 0) {
            ResponseData.error("修改剩余数量失败");
        }
        return ResponseData.success();
    }

    /**
     * 修改剩余数量
     * @param boList boList
     * @return ResponseData.class
     */
    @Override
    public ResponseData<Boolean> updateSyslByBoList(List<String> boList) {
        int i = baseMapper.updateSyslByBoList(boList);
        if (i <= 0) {
            ResponseData.error("修改剩余数量失败");
        }
        return ResponseData.success();
    }
}
