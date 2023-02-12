package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.mes.api.dto.UpkeepPlanDto;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import com.itl.iap.mes.api.entity.UpkeepUser;
import com.itl.iap.mes.provider.mapper.UpkeepUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yx
 * @date 2021/8/17
 * @since 1.8
 */
@Service
public class UpkeepUserServiceImpl extends ServiceImpl<UpkeepUserMapper, UpkeepUser> {

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveUpkeepUsers(UpkeepPlanDto upkeepPlanDto) {
        List<UpkeepUser> checkUsers = upkeepPlanDto.getUpkeepUsers();
        if (CollectionUtil.isNotEmpty(checkUsers)) {
            List<UpkeepUser> list = checkUsers.stream().distinct().map(x ->
                    {
                        UpkeepUser upkeepUser = new UpkeepUser();
                        upkeepUser.setUpkeepId(upkeepPlanDto.getId());
                        upkeepUser.setUpkeepUserId(x.getUpkeepUserId());
                        return upkeepUser;
                    }
            ).collect(Collectors.toList());
            this.saveBatch(list);
        }
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveUpkeepUsers(UpkeepPlan upkeepPlan) {
        upkeepPlan.getUpkeepUsers().stream().forEach(x -> x.setUpkeepId(upkeepPlan.getId()));
        this.saveBatch(upkeepPlan.getUpkeepUsers());
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveUpkeepUsers(List<UpkeepPlan> upkeepPlans) {
        if (upkeepPlans != null && upkeepPlans.size() > 0) {
            List<UpkeepUser> users = upkeepPlans.stream().flatMap(x ->
                    x.getUpkeepUsers().stream().map(y -> {
                        y.setUpkeepId(x.getId());
                        return y;
                    })
            ).collect(Collectors.toList());
            this.saveBatch(users);
        }
    }

    /**
     * 根据计划id查询保养人信息
     */
    public List<UpkeepUser> listByUpkeepPlanId(String planId) {
        return lambdaQuery().eq(UpkeepUser::getUpkeepId, planId).list();
    }
}
