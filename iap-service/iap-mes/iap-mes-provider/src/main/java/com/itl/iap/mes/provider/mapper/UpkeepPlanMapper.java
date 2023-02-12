package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.MesBaseMapper;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @time 2018年11月14日
 * @since JDK 1.8
 */
@Mapper
public interface UpkeepPlanMapper extends MesBaseMapper<UpkeepPlan> {

    IPage<UpkeepPlan> findList(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
    IPage<UpkeepPlan> findListByState(Page page, @Param("upkeepPlan") UpkeepPlan upkeepPlan);

    List<UpkeepPlan> findAllList(@Param(Constants.WRAPPER) QueryWrapper<CheckPlan> queryWrapper);
}
