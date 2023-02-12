package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetQueryDto;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetSaveDto;
import com.itl.mes.core.api.entity.ShopOrderBomComponnet;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/5/27
 */
public interface ShopOrderBomComponnetService extends IService<ShopOrderBomComponnet> {


    /**
     * 查询工单BOM信息/或者工序信息
     * @param shopOrderBomComponnetQueryDto
     * @return
     */
    IPage<ShopOrderBomComponnetVo> findList(ShopOrderBomComponnetQueryDto shopOrderBomComponnetQueryDto);

    /**
     * 根据shopOrderBo和type返回工单工序Bom
     * @param shopOrderBo 工单Bo
     * @param type {"OP"-工序,"SO"-工单}
     * @return
     */
    List<ShopOrderBomComponnetVo> queryBomByShopOrderBo(String shopOrderBo, String type);

    /**
     * 工单BOM保存/工序BOM保存
     * @param shopOrderBomComponnetSaveDto
     */
    void save(ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto) throws CommonException;


}
