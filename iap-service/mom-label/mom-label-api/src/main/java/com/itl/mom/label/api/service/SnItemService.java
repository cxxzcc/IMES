package com.itl.mom.label.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.entity.label.SnItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GKL
 * @date 2021/11/10 - 15:46
 * @since 2021/11/10 - 15:46 星期三 by GKL
 */
public interface SnItemService extends IService<SnItem> {
    /**
     * 查询
     * @param itemBo
     * @param snBo
     * @return
     */
    ResponseData<SnItem> querySnItemByItemBoAndSnBo(String itemBo, String snBo);

    /**
     * 修改剩余数量
     * @param id snbo
     * @param sysl sysl
     * @return ResponseData.class
     */
    ResponseData<Boolean> updateSysl(String id, BigDecimal sysl);

    /**
     * 通过列表修改sysl为0
     * @param boList boList
     * @return ResponseData.class
     */
    ResponseData<Boolean> updateSyslByBoList(List<String> boList);
}
