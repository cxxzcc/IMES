package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.TemporaryDataRetryLogDTO;
import com.itl.mes.core.api.entity.TemporaryDataRetryLog;
import com.itl.mes.core.api.service.TemporaryDataRetryLogService;
import com.itl.mes.core.provider.mapper.TemporaryDataRetryLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  过站暂存数据重传记录服务实现类
 * </p>
 *
 * @author dengou
 * @since 2021-12-08
 */
@Service
public class TemporaryDataRetryLogServiceImpl extends ServiceImpl<TemporaryDataRetryLogMapper, TemporaryDataRetryLog> implements TemporaryDataRetryLogService {


    @Override
    public Page<TemporaryDataRetryLogDTO> getPage(Map<String, Object> params) {
        Page<TemporaryDataRetryLogDTO> page = new QueryPage<>(params);
        List<TemporaryDataRetryLogDTO> list = baseMapper.getPage(page, params);
        if(CollUtil.isNotEmpty(list)) {
            list.forEach(e-> {
                e.setIsRetryFlag(StrUtil.isEmpty(e.getUpdateUser()) ? CommonConstants.FLAG_N : CommonConstants.FLAG_Y);
                e.setTypeDesc(TemporaryDataTypeEnum.parseDescByCode(e.getType()));
            });
        }
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean temporaryDataRetryLog(TemporaryDataRetryLog temporaryDataRetryLog) {
        TemporaryDataRetryLog entityByTemporaryDataId = selectByTemporaryDataId(temporaryDataRetryLog.getTemporaryDataId());
        if(entityByTemporaryDataId == null) {
            temporaryDataRetryLog.setId(null);
            temporaryDataRetryLog.setRetryCount(1);
        } else {
            temporaryDataRetryLog.setId(entityByTemporaryDataId.getId());
            temporaryDataRetryLog.setRetryCount(entityByTemporaryDataId.getRetryCount() + 1);
        }
        temporaryDataRetryLog.setUpdateTime(new Date());
        if(StrUtil.isBlank(temporaryDataRetryLog.getUpdateUser())) {
            temporaryDataRetryLog.setUpdateUser(UserUtils.getUserName());
        }
        return saveOrUpdate(temporaryDataRetryLog);
    }

    private TemporaryDataRetryLog selectByTemporaryDataId(String temporaryDataId) {
        return lambdaQuery()
                .eq(TemporaryDataRetryLog::getTemporaryDataId, temporaryDataId)
                .one();
    }
}
