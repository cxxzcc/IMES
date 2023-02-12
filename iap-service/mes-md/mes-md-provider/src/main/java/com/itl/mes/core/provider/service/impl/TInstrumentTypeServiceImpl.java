package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.mes.core.api.dto.TProjectQueryDTO;
import com.itl.mes.core.api.entity.TInstrumentType;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.service.TInstrumentTypeService;
import com.itl.mes.core.api.service.TProjectService;
import com.itl.mes.core.provider.mapper.TInstrumentTypeMapper;
import com.itl.mes.core.provider.mapper.TProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author cjq
 * @Date 2021/12/20 3:57 下午
 * @Description TODO
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TInstrumentTypeServiceImpl extends ServiceImpl<TInstrumentTypeMapper, TInstrumentType> implements TInstrumentTypeService {

}
