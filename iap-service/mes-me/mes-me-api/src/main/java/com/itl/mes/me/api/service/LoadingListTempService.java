package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.entity.LoadingList;
import com.itl.mes.me.api.entity.LoadingListTemp;

import java.util.List;

/**
 * 生产执行-批次条码绑定-上料清单temp
 *
 * @author cch
 * @date 2021-06-08
 */
public interface LoadingListTempService extends IService<LoadingListTemp> {

    /**
     * 获取批次绑定上料清单temp
     * @param productSn 产品条码
     * @param operationBo 工序Bo
     * @return
     */
    List<LoadingListTemp> getLoadingListTemp(String productSn, String operationBo);

    /**
     * 下料
     * @param ids 批次条码绑定临时上料表id
     * @return
     */
    Boolean unload(List<String> ids);
}