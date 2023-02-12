package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.mes.core.api.dto.TProjectActualQueryDTO;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.service.TProjectActualService;
import com.itl.mes.core.api.vo.TProjectActualVO;
import com.itl.mes.core.provider.mapper.TProjectActualMapper;
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
public class TProjectActualServiceImpl extends ServiceImpl<TProjectActualMapper, TProjectActual> implements TProjectActualService {

    @Override
    public IPage<TProjectActualVO> pageList(TProjectActualQueryDTO tProjectActualQueryDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(tProjectActualQueryDTO);
        return this.baseMapper.pageList(tProjectActualQueryDTO.getPage(), queryWrapper);
    }

    @Override
    public List<TProjectActualVO> allList(TProjectActualQueryDTO tProjectActualQueryDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(tProjectActualQueryDTO);
        return this.baseMapper.pageList(queryWrapper);
    }
}
