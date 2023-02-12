package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.dto.TDeviceActualQueryDTO;
import com.itl.mes.core.api.entity.TDeviceActual;
import com.itl.mes.core.api.service.TDeviceActualService;
import com.itl.mes.core.api.vo.TDeviceActualVO;
import com.itl.mes.core.provider.mapper.TDeviceActualMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author cjq
 * @Date 2021/12/20 3:57 下午
 * @Description TODO
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TDeviceActualServiceImpl extends ServiceImpl<TDeviceActualMapper, TDeviceActual> implements TDeviceActualService {

    @Override
    public IPage<TDeviceActualVO> pageList(TDeviceActualQueryDTO tDeviceActualQueryDTO) {
        QueryWrapper<TDeviceActual> queryWrapper = new QueryWrapper<>();
        String startTime = tDeviceActualQueryDTO.getStartTime();
        String endTime = tDeviceActualQueryDTO.getEndTime();
        if (StrUtil.isNotBlank(startTime) && StrUtil.isNotBlank(endTime)) {
            queryWrapper.lambda().between(TDeviceActual::getUse_date, startTime, endTime);
        }
        return this.baseMapper.pageList(tDeviceActualQueryDTO.getPage(), queryWrapper);
    }
}
