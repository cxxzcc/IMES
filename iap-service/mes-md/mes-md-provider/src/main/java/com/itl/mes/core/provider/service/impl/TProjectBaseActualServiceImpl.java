package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.dto.TProjectBaseActualQueryDTO;
import com.itl.mes.core.api.entity.TProjectBaseActual;
import com.itl.mes.core.api.service.TProjectBaseActualService;
import com.itl.mes.core.api.vo.TProjectBaseActualVO;
import com.itl.mes.core.provider.mapper.TProjectBaseActualMapper;
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
public class TProjectBaseActualServiceImpl extends ServiceImpl<TProjectBaseActualMapper, TProjectBaseActual> implements TProjectBaseActualService {

    @Override
    public IPage<TProjectBaseActualVO> pageList(TProjectBaseActualQueryDTO tProjectBaseActualQueryDTO) {
        QueryWrapper<TProjectBaseActual> queryWrapper = gettProjectBaseActualQueryWrapper(tProjectBaseActualQueryDTO);
        return this.baseMapper.pageList(tProjectBaseActualQueryDTO.getPage(), queryWrapper);
    }

    @Override
    public List<TProjectBaseActualVO> allList(TProjectBaseActualQueryDTO tProjectBaseActualQueryDTO) {
        QueryWrapper<TProjectBaseActual> queryWrapper = gettProjectBaseActualQueryWrapper(tProjectBaseActualQueryDTO);
        return this.baseMapper.pageList(queryWrapper);
    }

    private QueryWrapper<TProjectBaseActual> gettProjectBaseActualQueryWrapper(TProjectBaseActualQueryDTO tProjectBaseActualQueryDTO) {
        QueryWrapper<TProjectBaseActual> queryWrapper = new QueryWrapper<>();
        String baseCodeOrName = tProjectBaseActualQueryDTO.getBaseCodeOrName();
        String projectCodeOrName = tProjectBaseActualQueryDTO.getProjectCodeOrName();
        if (StrUtil.isNotBlank(baseCodeOrName)) {
            queryWrapper.apply("( d.code like CONCAT('%',{0},'%') or d.name like CONCAT('%',{1},'%'))", baseCodeOrName, baseCodeOrName);
        }
        if (StrUtil.isNotBlank(projectCodeOrName)) {
            queryWrapper.apply("( a.code like CONCAT('%',{0},'%') or a.name like CONCAT('%',{1},'%'))", projectCodeOrName, projectCodeOrName);
        }
        String startTime = tProjectBaseActualQueryDTO.getStartTime();
        String endTime = tProjectBaseActualQueryDTO.getEndTime();
        if (StrUtil.isNotBlank(startTime) && StrUtil.isNotBlank(endTime)) {
            queryWrapper.lambda().between(TProjectBaseActual::getUse_date, startTime, endTime);
        }
        return queryWrapper;
    }
}
