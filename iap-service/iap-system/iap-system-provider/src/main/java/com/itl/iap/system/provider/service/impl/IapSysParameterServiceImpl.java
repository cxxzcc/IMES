package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.system.api.entity.IapSysParameter;
import com.itl.iap.system.api.service.IapSysParameterService;
import com.itl.iap.system.provider.mapper.IapSysParameterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IapSysParameterServiceImpl extends ServiceImpl<IapSysParameterMapper, IapSysParameter> implements IapSysParameterService {

    @Override
    public IPage<IapSysParameter> pageList(Page page, QueryWrapper queryWrapper) {
        IPage<IapSysParameter> pages =  this.baseMapper.pageList(page, queryWrapper);
        return pages;
    }
}
