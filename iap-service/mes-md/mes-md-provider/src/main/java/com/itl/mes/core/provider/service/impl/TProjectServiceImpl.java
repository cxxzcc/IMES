package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.mes.core.api.dto.TProjectQueryDTO;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.service.TProjectService;
import com.itl.mes.core.provider.mapper.TProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author cjq
 * @Date 2021/12/20 3:57 下午
 * @Description TODO
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TProjectServiceImpl extends ServiceImpl<TProjectMapper, TProject> implements TProjectService {

    private final TProjectMapper tProjectMapper;

    @Override
    public IPage<TProject> pageList(TProjectQueryDTO tProjectQueryDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(tProjectQueryDTO);
        return tProjectMapper.pageList(tProjectQueryDTO.getPage(), queryWrapper);
    }

    @Override
    public List<TProject> allList(TProjectQueryDTO tProjectQueryDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(tProjectQueryDTO);
        return tProjectMapper.pageList(queryWrapper);
    }
}
