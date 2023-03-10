package com.itl.iap.system.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.entity.IapSysAuthResourceT;

import java.util.List;

/**
 * 用户菜单service
 *
 * @author 谭强
 * @date 2020-06-22
 * @since jdk1.8
 */
public interface IapSysAuthResourceTService extends IService<IapSysAuthResourceT> {

    /**
     * 权限菜单添加
     * @param authResourceTs 权限角色实例
     * @param authId 权限id
     * @param clientType pc/app识别
     * @return boolean
     */
    boolean saveAuthResource(List<IapSysAuthResourceT> authResourceTs, String authId, String clientType);

    /**
     * 权限列表菜单查询
     * @param authId 权限id
     * @param clientType pc/app识别
     * @return
     */
    List<IapSysAuthResourceT> selectSource(String authId, String clientType);
}
