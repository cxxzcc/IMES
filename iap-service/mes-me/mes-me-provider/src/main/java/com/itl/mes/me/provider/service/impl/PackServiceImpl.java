package com.itl.mes.me.provider.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.me.api.entity.Pack;
import com.itl.mes.me.api.service.PackService;
import com.itl.mes.me.provider.mapper.PackMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 包装
 *
 * @author cch
 * @date 2021-06-16
 */
@Service
public class PackServiceImpl extends ServiceImpl<PackMapper, Pack> implements PackService {

    @Override
    public List<Pack> listAlreadyPack(String shopOrderBo, String operationBo) {
        List<Pack> ret = lambdaQuery().eq(Pack::getShopOrderBo, shopOrderBo).eq(Pack::getOperationBo, operationBo).list();
        if (ret == null || ret.size() == 0) {
            return Collections.emptyList();
        }
        return ret;
    }
}
