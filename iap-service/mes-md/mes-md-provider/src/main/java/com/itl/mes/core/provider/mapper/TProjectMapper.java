package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.entity.TProject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TProjectMapper extends BaseMapper<TProject> {

    IPage<TProject> pageList(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<TProject> pageList(@Param(Constants.WRAPPER) Wrapper wrapper);
}
