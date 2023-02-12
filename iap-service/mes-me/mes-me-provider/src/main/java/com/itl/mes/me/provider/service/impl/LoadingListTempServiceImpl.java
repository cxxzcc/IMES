package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.client.service.ItemFeignService;
import com.itl.mes.me.api.entity.LoadingList;
import com.itl.mes.me.api.entity.LoadingListTemp;
import com.itl.mes.me.api.service.LoadingListService;
import com.itl.mes.me.api.service.LoadingListTempService;
import com.itl.mes.me.provider.mapper.LoadingListMapper;
import com.itl.mes.me.provider.mapper.LoadingListTempMapper;
import com.itl.mom.label.client.service.SnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生产执行-批次条码绑定-上料清单
 *
 * @author cch
 * @date 2021-06-08
 */
@Service
public class LoadingListTempServiceImpl extends ServiceImpl<LoadingListTempMapper, LoadingListTemp> implements LoadingListTempService {
    private ItemFeignService itemFeignService;
    private SnService snService;

    @Autowired
    public void setItemFeignService(ItemFeignService itemFeignService) {
        this.itemFeignService = itemFeignService;
    }

    @Autowired
    public void setSnService(SnService snService) {
        this.snService = snService;
    }

    @Override
    public List<LoadingListTemp> getLoadingListTemp(String productSn, String operationBo) {
        List<LoadingListTemp> ret = super.lambdaQuery().eq(LoadingListTemp::getSfc, productSn)
                .eq(LoadingListTemp::getOperationBo, operationBo)
                .list();
        if (ret == null || ret.size() == 0) {
            return new ArrayList<>();
        }
        Set<String> itemBoSet = ret.stream().map(LoadingListTemp::getComponentBo).filter(Objects::nonNull).collect(Collectors.toSet());
        List<Item> itemList = itemFeignService.getItemList(itemBoSet);
        if (itemList != null && itemList.size() > 0) {
            Map<String, Item> itemMap = itemList.stream().collect(Collectors.toMap(Item::getBo, x -> x));
            ret.forEach(x -> {
                Item item = Optional.ofNullable(itemMap.get(x.getComponentBo())).orElseGet(Item::new);
                x.setItem(item.getItem());
                x.setItemDesc(item.getItemDesc());
            });
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Boolean unload(List<String> ids) {
        Map<String, String> snStateMap = this.listByIds(ids).stream().collect(Collectors.toMap(LoadingListTemp::getAssembledSn, LoadingListTemp::getSnOriginState));
        boolean a = this.removeByIds(ids);
        boolean b = Optional.ofNullable(snService.changeSnStateByMap(snStateMap)).orElse(false);
        return a && b;
    }
}
