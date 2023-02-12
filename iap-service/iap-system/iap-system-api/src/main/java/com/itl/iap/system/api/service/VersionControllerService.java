//package com.itl.iap.system.api.service;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.itl.iap.system.api.entity.VersionController;
//
//import java.util.Map;
//
//public interface VersionControllerService extends IService<VersionController> {
//    /**
//     * 根据传入的app版本号获取数据库中对应数据,如果不为最新，则附加最新的app下载链接，反则不附加
//     *
//     * @param versionNo APP版本号
//     * @return CfAppVersionControl
//     * @throws Exception
//     */
//    VersionController checkAppVersion(String versionNo,Integer type) throws Exception;
//
//    IPage<Map<String, Object>> selectPageVersionForTable(IPage<Map<String, Object>> page, Map<String, Object> params);
//
//    /**
//     * 将所有版本记录设置为非最新版本
//     */
//    void setAllRecordNotUpToDate();
//
//    /**
//     * 发布新版本
//     * @param userId
//     * @param versionController
//     */
//    void addNewAppVersion(VersionController versionController,String userId);
//
//    /**
//     * 设置版本为默认最新版本
//     * @param versionController
//     * @param userId
//     */
//    void setUpToDate(VersionController versionController, String userId);
//
//}
