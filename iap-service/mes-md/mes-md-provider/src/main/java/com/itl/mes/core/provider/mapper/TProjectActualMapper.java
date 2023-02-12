package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.MesBaseMapper;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.vo.TProjectActualVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TProjectActualMapper extends MesBaseMapper<TProjectActual> {

    IPage<TProjectActualVO> pageList(Page page, @Param(Constants.WRAPPER) QueryWrapper<TProjectActual> queryWrapper);

    List<TProjectActualVO> pageList(@Param(Constants.WRAPPER) QueryWrapper<TProjectActual> queryWrapper);
}
