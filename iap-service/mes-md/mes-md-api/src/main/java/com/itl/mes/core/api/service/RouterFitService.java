package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.RouterFitDto;
import com.itl.mes.core.api.entity.RouterFit;


/**
 * 工艺路线设置表
 *
 * @author xtz
 * @date 2021-05-25
 */
public interface RouterFitService extends IService<RouterFit> {

    IPage<RouterFitDto> queryByItem(RouterFitDto routerFitDto);

    IPage<RouterFitDto> queryByItemGroup(RouterFitDto routerFitDto);

    IPage<RouterFitDto> queryByProductLine( RouterFitDto routerFitDto);

    boolean add (RouterFit routerFit) throws CommonException;

    boolean update (RouterFit routerFit) throws CommonException;

    void delete (RouterFit routerFit);

    RouterFitDto getRouterAndBom (String orderType, String itemBo, String productBo);
}

