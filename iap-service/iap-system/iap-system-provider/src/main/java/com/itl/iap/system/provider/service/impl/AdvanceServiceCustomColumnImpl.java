package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.system.api.bo.UserCustomColumnConfigBo;
import com.itl.iap.system.api.dto.UserCustomColumnDto;
import com.itl.iap.system.api.entity.UserCustomColumnConfig;
import com.itl.iap.system.api.service.AdvanceServiceCustomColumn;
import com.itl.iap.system.provider.mapper.UserCustomColumnConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户自定义列查询 业务层
 */
@Service
public class AdvanceServiceCustomColumnImpl extends ServiceImpl<UserCustomColumnConfigMapper, UserCustomColumnConfig> implements AdvanceServiceCustomColumn {

    /**
     * 如果用户不配置走默认
     * 如果用户配置就保存或者更新
     */
    @Override
    public Boolean saveOrUpdateById(UserCustomColumnDto customColumnDto) {
        UserTDto currentUser = UserUtils.getCurrentUser();
        if (null == currentUser) throw new RuntimeException("请登录");
        UserCustomColumnConfigBo bo = new UserCustomColumnConfigBo(currentUser.getId(), customColumnDto.getPageId());

        /*1.高级查询拼接的查询字段包括AS关键字 2.要显示的字段名*/
        List<String> showColumnList = customColumnDto.getShowColumnList();
        String showColumnLis = showColumnList.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(","));

        // 保存或更新
        UserCustomColumnConfig userConfig = new UserCustomColumnConfig();
        userConfig.setBo(bo.getBo())
                .setPageId(bo.getPageId())
                .setUserId(bo.getUserId())
                .setShowColumn(showColumnLis)
                .setIsSort(customColumnDto.getIsSort());
        return saveOrUpdate(userConfig);
    }

    @Override
    public String queryShowColumns(String pageID) {
        UserTDto currentUser = UserUtils.getCurrentUser();
        if (null == currentUser) throw new RuntimeException("请登录");
        UserCustomColumnConfigBo bo = new UserCustomColumnConfigBo(currentUser.getId(), pageID);
        UserCustomColumnConfig byIdConfig = getById(bo.getBo());
        if (byIdConfig == null) {
            //throw new RuntimeException("无自定义列配置");
            return "";
        } else {
            String showColumn = byIdConfig.getShowColumn();
            if (StringUtils.isEmpty(showColumn)) {
                throw new RuntimeException("无自定义列配置");
            } else {
                return showColumn;
            }
        }

    }

    @Override
    public Boolean deleteUserShowColumn(String userId, String pageID) {
        Assert.valid(StrUtil.isBlank(userId), "请登录");
        UserCustomColumnConfig one = lambdaQuery().eq(UserCustomColumnConfig::getUserId, userId).eq(UserCustomColumnConfig::getPageId, pageID).one();
        if(one != null) {
            return removeById(one.getBo());
        }
        return false;
    }
}
