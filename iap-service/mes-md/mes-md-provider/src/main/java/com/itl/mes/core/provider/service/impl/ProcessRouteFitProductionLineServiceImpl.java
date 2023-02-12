package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.ProcessRouteFitProductGroup;
import com.itl.mes.core.api.entity.ProcessRouteFitProductionLine;
import com.itl.mes.core.api.service.IProcessRouteFitProductionLineService;
import com.itl.mes.core.provider.mapper.ProcessRouteFitProductionLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 产线工艺路线设置表 服务实现类
 * </p>
 *
 * @author lK
 * @since 2021-10-15
 */
@Service
public class ProcessRouteFitProductionLineServiceImpl extends ServiceImpl<ProcessRouteFitProductionLineMapper, ProcessRouteFitProductionLine> implements IProcessRouteFitProductionLineService {

    @Autowired
    private ProcessRouteFitProductionLineMapper lineMapper;

    @Override
    public IPage<Object> getProductLineRoute(Map<String, Object> map) {
        Integer current = Optional.ofNullable((Integer) map.get("current")).orElse(1);
        Integer size = Optional.ofNullable((Integer) map.get("size")).orElse(10);
        Page<Object> page = new Page<Object>(current.longValue(), size.longValue());

        map.put("site", UserUtils.getSite());

        return lineMapper.getProductLineRoute(page, map);
    }

    @Override
    public List<ProcessRouteFitProductionLine> getProcessRouteFitProductionLine(ProcessRouteFitProductionLine processRouteFitProductionLine) {
        processRouteFitProductionLine.setSite(UserUtils.getSite());
        QueryWrapper<ProcessRouteFitProductionLine> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(processRouteFitProductionLine);
        return lineMapper.selectList(queryWrapper);
    }

}
