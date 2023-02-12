package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.service.ITemporaryDataService;
import com.itl.mes.core.provider.mapper.TemporaryDataMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Service
public class TemporaryDataServiceImpl
        extends ServiceImpl<TemporaryDataMapper, TemporaryData> implements ITemporaryDataService {

    @Override
    public Boolean addOrUpdate(TemporaryData temporaryData) {
        //判断是否存在
        TemporaryData one = lambdaQuery().eq(TemporaryData::getSn, temporaryData.getSn())
                .eq(TemporaryData::getType, temporaryData.getType())
                .eq(TemporaryData::getStation, temporaryData.getStation()).one();
        if(one == null) {
            temporaryData.setCreateUser(UserUtils.getUserName());
            temporaryData.setCreateTime(new Date());
            return save(temporaryData);
        }
        temporaryData.setId(one.getId());
        temporaryData.setUpdateUser(UserUtils.getUserName());
        temporaryData.setUpdateTime(new Date());
        return updateById(temporaryData);
    }

    @Override
    public Boolean removeBySns(List<String> sns, String station, String type) {
        if(CollUtil.isEmpty(sns)) {
            return false;
        }
        List<TemporaryData> list = lambdaQuery().select(TemporaryData::getId).in(TemporaryData::getSn, sns).eq(TemporaryData::getStation, station)
                .eq(TemporaryData::getType, type)
                .list();
        if(CollUtil.isEmpty(list)) {
            return false;
        }
        List<String> ids = list.stream().map(TemporaryData::getId).collect(Collectors.toList());
        return removeByIds(ids);
    }

    @Override
    public Boolean removeListBySns(List<String> sns, String station, String type) {
        if(StrUtil.isBlank(type)) {
            return false;
        }
        List<String> types = StrUtil.splitTrim(type, StrUtil.COMMA);
        List<TemporaryData> list = lambdaQuery().select(TemporaryData::getId)
                .in(TemporaryData::getSn, sns)
                .eq(TemporaryData::getStation, station)
                .in(TemporaryData::getType, types)
                .list();
        if(CollUtil.isEmpty(list)) {
            return false;
        }
        List<String> ids = list.stream().map(TemporaryData::getId).collect(Collectors.toList());
        return removeByIds(ids);
    }

    @Override
    public TemporaryData getBySnAndStation(String sn, String station, String type) {
        return lambdaQuery().eq(TemporaryData::getSn, sn).eq(TemporaryData::getStation, station).eq(TemporaryData::getType, type).one();
    }

    @Override
    public List<TemporaryData> getBySnAndStation(String sn, String station, List<String> types) {
        return lambdaQuery().eq(TemporaryData::getSn, sn).eq(TemporaryData::getStation, station).in(TemporaryData::getType, types).list();
    }
}
