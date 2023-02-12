package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.UserCustomColumnDto;
import com.itl.iap.system.api.entity.UserCustomColumnConfig;

/**
 * 用户自定列 业务层接口
 */
public interface AdvanceServiceCustomColumn extends IService<UserCustomColumnConfig> {
    /**
     * 更新或修改用户的配置
     */
    Boolean saveOrUpdateById(UserCustomColumnDto customColumnDto);

    /**
     * @param pageID 页面标识
     */
    String queryShowColumns(String pageID);

    /**
     * 删除用户配置
     * @param pageID 页面id
     */
    Boolean deleteUserShowColumn(String userId, String pageID);

    /**
     * 预留其他方法
     */
}
