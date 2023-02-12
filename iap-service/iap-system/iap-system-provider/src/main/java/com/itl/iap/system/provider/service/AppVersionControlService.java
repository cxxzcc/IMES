package com.itl.iap.system.provider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.entity.AppVersionControl;

/**
 * APP版本控制业务层接口
 *
 * @author yezi
 * @date 2019/5/29
 */
public interface AppVersionControlService extends IService<AppVersionControl> {
    /**
     * 根据传入的app版本号获取数据库中对应数据,如果不为最新，则附加最新的app下载链接，反则不附加
     *
     * @param appVersionNo APP版本号
     * @return CfAppVersionControl
     * @throws Exception
     */
    AppVersionControl checkAppVersion(String appVersionNo,String systemType) throws Exception;

    /**
     * 将所有版本记录设置为非最新版本（分类型）
     */
    void setAllRecordNotUpToDate(String systemType);

    /**
     * 发布新版本
     * @param cfAppVersionControl
     */
    void addNewAppVersion(AppVersionControl cfAppVersionControl);

    /**
     * 设置版本为默认最新版本
     * @param appVersionControl
     */
    void setUpToDate(AppVersionControl appVersionControl);
}
