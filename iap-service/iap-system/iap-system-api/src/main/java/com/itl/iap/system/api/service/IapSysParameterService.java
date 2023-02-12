package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.entity.IapSysParameter;

public interface IapSysParameterService extends IService<IapSysParameter> {

    IPage<IapSysParameter> pageList(Page page, QueryWrapper queryWrapper);
}
