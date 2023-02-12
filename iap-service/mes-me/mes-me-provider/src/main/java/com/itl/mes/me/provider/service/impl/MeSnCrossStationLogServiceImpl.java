package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.me.api.entity.MeSnCrossStationLog;
import com.itl.mes.me.api.service.MeSnCrossStationLogService;
import com.itl.mes.me.provider.mapper.MeSnCrossStationLogMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 条码过站记录 服务实现类
 * </p>
 *
 * @author dengou
 * @since 2021-12-07
 */
@Service
public class MeSnCrossStationLogServiceImpl extends ServiceImpl<MeSnCrossStationLogMapper, MeSnCrossStationLog> implements MeSnCrossStationLogService {

    @Override
    public Boolean addLog(MeSnCrossStationLog meSnCrossStationLog) {
        Assert.valid(StrUtil.isBlank(meSnCrossStationLog.getSn()), "条码不能为空");
        Assert.valid(StrUtil.isBlank(meSnCrossStationLog.getOperationBo()), "工序不能为空");

        meSnCrossStationLog.setId(null);
        if(StrUtil.isBlank(meSnCrossStationLog.getStation())) {
            meSnCrossStationLog.setStation(UserUtils.getStation());
        }
        if(StrUtil.isBlank(meSnCrossStationLog.getSite())) {
            meSnCrossStationLog.setSite(UserUtils.getSite());
        }
        if(StrUtil.isBlank(meSnCrossStationLog.getCreateUser())) {
            meSnCrossStationLog.setCreateUser(UserUtils.getUserName());
        }
        meSnCrossStationLog.setCreateTime(new Date());

        return save(meSnCrossStationLog);
    }

    @Override
    public Integer getCountBySn(String sn, String site, String operationBo) {
        return lambdaQuery().eq(MeSnCrossStationLog::getSn, sn)
                .eq(MeSnCrossStationLog::getSite, site)
                .eq(MeSnCrossStationLog::getOperationBo, operationBo)
                .count();
    }
}
