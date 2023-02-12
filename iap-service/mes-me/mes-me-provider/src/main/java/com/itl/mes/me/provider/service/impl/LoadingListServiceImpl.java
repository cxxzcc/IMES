package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.client.service.ItemFeignService;
import com.itl.mes.me.api.entity.LoadingList;
import com.itl.mes.me.api.service.LoadingListService;
import com.itl.mes.me.provider.mapper.LoadingListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生产执行-批次条码绑定-上料清单
 *
 * @author cch
 * @date 2021-06-08
 */
@Service
public class LoadingListServiceImpl extends ServiceImpl<LoadingListMapper, LoadingList> implements LoadingListService {
    private ItemFeignService itemFeignService;

    @Autowired
    public void setItemFeignService(ItemFeignService itemFeignService) {
        this.itemFeignService = itemFeignService;
    }

    @Override
    public List<LoadingList> getLoadingList(String productSn, String operationBo) {
        List<LoadingList> ret = super.lambdaQuery().eq(LoadingList::getSfc, productSn)
                .eq(LoadingList::getOperationBo, operationBo)
                .list();
        if (ret == null || ret.size() == 0) {
            return new ArrayList<>();
        }
        Set<String> itemBoSet = ret.stream().map(LoadingList::getComponentBo).filter(Objects::nonNull).collect(Collectors.toSet());
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
}
