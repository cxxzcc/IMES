package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.entity.Pack;

import java.util.List;

/**
 * 包装
 *
 * @author cch
 * @date 2021-06-16
 */
public interface PackService extends IService<Pack> {

    /**
     * 根据工单Bo和工序Bo查询已经保存的包装信息, 如果为空, 返回emptyList
     * @param shopOrderBo
     * @param operationBo
     * @return
     */
    List<Pack> listAlreadyPack(String shopOrderBo, String operationBo);
}

