package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.entity.LoadingList;

import java.util.Comparator;
import java.util.List;

/**
 * 生产执行-批次条码绑定-上料清单
 *
 * @author cch
 * @date 2021-06-08
 */
public interface LoadingListService extends IService<LoadingList> {

    /**
     * 获取批次绑定上料清单
     * @param productSn 产品条码
     * @param operationBo 工序Bo
     * @return
     */
    List<LoadingList> getLoadingList(String productSn, String operationBo);
}