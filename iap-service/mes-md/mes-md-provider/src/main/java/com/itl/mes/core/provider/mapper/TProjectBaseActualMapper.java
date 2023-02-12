package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.MesBaseMapper;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.entity.TProjectBaseActual;
import com.itl.mes.core.api.vo.TProjectActualVO;
import com.itl.mes.core.api.vo.TProjectBaseActualVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TProjectBaseActualMapper extends MesBaseMapper<TProjectBaseActual> {

    IPage<TProjectBaseActualVO> pageList(Page page, @Param(Constants.WRAPPER) QueryWrapper<TProjectBaseActual> queryWrapper);

    List<TProjectBaseActualVO> pageList(@Param(Constants.WRAPPER) QueryWrapper<TProjectBaseActual> queryWrapper);
}
