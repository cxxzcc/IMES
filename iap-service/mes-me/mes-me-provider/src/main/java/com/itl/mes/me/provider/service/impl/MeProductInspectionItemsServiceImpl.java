package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.me.api.entity.MeProductInspectionItemsEntity;
import com.itl.mes.me.api.service.MeProductInspectionItemsService;
import com.itl.mes.me.provider.mapper.MeProductInspectionItemsMapper;
import org.springframework.stereotype.Service;


@Service("meProductInspectionItemsService")
public class MeProductInspectionItemsServiceImpl extends ServiceImpl<MeProductInspectionItemsMapper, MeProductInspectionItemsEntity> implements MeProductInspectionItemsService {



}