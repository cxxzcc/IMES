package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.mes.api.dto.CheckPlanDto;
import com.itl.iap.mes.api.entity.CheckPlanUser;
import com.itl.iap.mes.provider.mapper.CheckPlanUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yx
 * @date 2021/8/17
 * @since 1.8
 */
@Service
public class CheckPlanUserServiceImpl extends ServiceImpl<CheckPlanUserMapper, CheckPlanUser> {

    @Transactional
    public void saveCheckPlanUsers(CheckPlanDto checkPlanDto) {
        List<CheckPlanUser> checkUsers = checkPlanDto.getCheckUsers();
        if (CollectionUtil.isNotEmpty(checkUsers)) {
            List<CheckPlanUser> list = checkUsers.stream().distinct().map(x -> {
                        CheckPlanUser checkPlanUser = new CheckPlanUser();
                        checkPlanUser.setCheckId(checkPlanDto.getId());
                        checkPlanUser.setCheckUserId(x.getCheckUserId());
                        return checkPlanUser;
                    }
            ).collect(Collectors.toList());
            this.saveBatch(list);
        }
    }

    /**
     * 根据计划id查询点检人信息
     */
    public List<CheckPlanUser> listByCheckPlanId(String planId) {
        return lambdaQuery().eq(CheckPlanUser::getCheckId, planId).list();
    }
}
