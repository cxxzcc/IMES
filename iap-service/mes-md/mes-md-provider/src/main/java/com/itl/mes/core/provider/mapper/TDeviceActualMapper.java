package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.MesBaseMapper;
import com.itl.mes.core.api.entity.TDeviceActual;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.vo.TDeviceActualVO;
import com.itl.mes.core.api.vo.TProjectActualVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TDeviceActualMapper extends MesBaseMapper<TDeviceActual> {

    IPage<TDeviceActualVO> pageList(Page page, @Param(Constants.WRAPPER) QueryWrapper<TDeviceActual> queryWrapper);

    List<TDeviceActualVO> pageList(@Param(Constants.WRAPPER) QueryWrapper<TDeviceActual> queryWrapper);
}
