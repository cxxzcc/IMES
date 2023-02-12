package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.TBase;
import com.itl.mes.core.api.service.TBaseService;
import com.itl.mes.core.provider.mapper.TBaseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author cjq
 * @Date 2021/12/20 3:57 下午
 * @Description TODO
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TBaseServiceImpl extends ServiceImpl<TBaseMapper, TBase> implements TBaseService {


}
