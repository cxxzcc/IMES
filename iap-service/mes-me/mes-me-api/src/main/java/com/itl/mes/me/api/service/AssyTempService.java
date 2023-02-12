package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.me.api.entity.AssyTemp;

import java.util.List;

/**
 * 装配临时表
 *
 * @author cch
 * @date 2021-06-08
 */
public interface AssyTempService extends IService<AssyTemp> {

    void saveAssyTempInfo(String productSn, List<ShopOrderBomComponnetVo> list);
}