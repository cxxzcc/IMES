package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.PageUtils;
import com.itl.mes.me.api.entity.MeProductGroupInspectionItemsEntity;
import com.itl.mes.me.api.service.MeProductGroupInspectionItemsService;
import com.itl.mes.me.provider.mapper.MeProductGroupInspectionItemsMapper;
import org.springframework.stereotype.Service;

import java.util.Map;



@Service("meProductGroupInspectionItemsService")
public class MeProductGroupInspectionItemsServiceImpl extends ServiceImpl<MeProductGroupInspectionItemsMapper, MeProductGroupInspectionItemsEntity> implements MeProductGroupInspectionItemsService {


}