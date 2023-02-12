package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.ProcessRouteFitProduct;
import com.itl.mes.core.api.entity.ProcessRouteFitProductGroup;
import com.itl.mes.core.api.service.IProcessRouteFitProductGroupService;
import com.itl.mes.core.provider.mapper.ProcessRouteFitProductGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 产品组工艺路线设置表 服务实现类
 * </p>
 *
 * @author lK
 * @since 2021-10-15
 */
@Service
public class ProcessRouteFitProductGroupServiceImpl extends ServiceImpl<ProcessRouteFitProductGroupMapper, ProcessRouteFitProductGroup> implements IProcessRouteFitProductGroupService {

    @Autowired
    private ProcessRouteFitProductGroupMapper groupMapper;

    @Override
    public IPage<Object> getGroupRoute(Map<String, Object> map) {
        Integer current = Optional.ofNullable((Integer) map.get("current")).orElse(1);
        Integer size = Optional.ofNullable((Integer) map.get("size")).orElse(10);
        Page<Object> page = new Page<>(current.longValue(), size.longValue());

        map.put("site", UserUtils.getSite());

        return groupMapper.getGroupRoute(page, map);
    }

    @Override
    public List<ProcessRouteFitProductGroup> getProcessRouteFitProductGroup(ProcessRouteFitProductGroup processRouteFitProductGroup) {
        processRouteFitProductGroup.setSite(UserUtils.getSite());
        QueryWrapper<ProcessRouteFitProductGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(processRouteFitProductGroup);
        return groupMapper.selectList(queryWrapper);
    }

}
