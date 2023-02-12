package com.itl.mes.me.provider.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.me.api.entity.MeSnDataCollection;
import com.itl.mes.me.api.service.MeSnDataCollectionService;
import com.itl.mes.me.provider.mapper.MeSnDataCollectionMapper;
import org.springframework.stereotype.Service;

/**
 * (MeSnDataCollection)表服务实现类
 *
 * @author makejava
 * @since 2021-06-02 15:06:27
 */
@Service("meSnDataCollectionService")
public class MeSnDataCollectionServiceImpl extends ServiceImpl<MeSnDataCollectionMapper, MeSnDataCollection> implements MeSnDataCollectionService {

}
