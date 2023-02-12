package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.TProjectQueryDTO;
import com.itl.mes.core.api.entity.TBase;
import com.itl.mes.core.api.entity.TProject;

import java.util.List;

public interface TProjectService extends IService<TProject> {

    IPage<TProject> pageList(TProjectQueryDTO tProjectQueryDTO);

    List<TProject> allList(TProjectQueryDTO tProjectQueryDTO);
}