//package com.itl.iap.system.provider.service.impl;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//
//import com.itl.iap.common.base.utils.UserUtils;
//import com.itl.iap.mes.api.entity.DataCollectionType;
//import com.itl.iap.system.api.entity.AppVersionControl;
//import com.itl.iap.system.api.entity.VersionController;
//import com.itl.iap.system.api.service.VersionControllerService;
//import com.itl.iap.system.provider.mapper.VersionControllerMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * APP版本控制业务层接口实现类
// *
// * @author yezi
// * @date 2019/5/29
// */
//@Service
//public class VersionControllerServiceImpl extends ServiceImpl<VersionControllerMapper, AppVersionControl> implements VersionControllerService {
//
//    @Autowired
//    private VersionControllerMapper versionControllerMapper;
//
//   @Override
//    public AppVersionControl checkAppVersion(String versionNo, Integer type) throws Exception {
//       AppVersionControl queryCondition = new AppVersionControl();
//       queryCondition.setVersionNo(versionNo);
//       queryCondition.setType(type);
//        //获取对应版本号的app版本控制数据
//        QueryWrapper<VersionController> entityWrapper = new QueryWrapper<>(queryCondition);
//        VersionController versionController1 = getOne(entityWrapper);
//       if (versionController1 == null) {
//            throw new Exception("当前APP版本号:" + versionNo + "没有被维护,请联系系统管理员!!!");
//        }
//
//        //如果当前版本不为最新，则返回最新版本app下载链接
//        if (versionController1.getIsUpToDate() == 0) {
//            queryCondition.setIsUpToDate((byte)1);
//            QueryWrapper<VersionController> entityWrapper2 = new QueryWrapper<>(queryCondition);
//            VersionController versionController2 = getOne(entityWrapper);
//            if (versionController2 == null) {
//                throw new Exception("APP更新失败!!!原因为:系统没有维护最新的APP版本,请联系管理员!!!");
//            }
//            if(queryCondition.getType() == 1001) {
//                queryCondition.setLatestDownloadLink(versionController2.getDownLoadLink());
//            }if(queryCondition.getType()==1002){
//                queryCondition.setLatestDownloadLink(null);
//            }
//        }
//        return versionController1;
//    }
//
//    @Override
//    public IPage<Map<String, Object>> selectPageVersionForTable(IPage<Map<String, Object>> page, Map<String, Object> params) {
//        if (params != null && !params.containsKey("site")) {
//            params.put("site", UserUtils.getSite());
//        }
//        List<Map<String, Object>> maps = versionControllerMapper.selectPageVersionForTable(page, params);
//        page.setRecords(maps);
//        return page;
//    }
//    @Override
//    public void setAllRecordNotUpToDate() {
//        versionControllerMapper.setAllRecordNotUpToDate();
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public void addNewAppVersion(VersionController versionController, String userId) {
//        //设置所有记录不为最新版本
//        this.setAllRecordNotUpToDate();
//
//        //插入新的版本记录
//        versionController.setObjectBasicAttributes(userId, new Date());
//        //默认为最新版本
//        versionController.setIsUpToDate((byte) 1);
//        versionControllerMapper.insert(versionController);
//    }
//
//    @Override
//    public void setUpToDate(VersionController versionController, String userId) {
//        this.setAllRecordNotUpToDate();
//        versionController.setIsUpToDate((byte) 1);
//        versionController.setObjectBasicAttributes(userId, new Date());
//        this.updateById(versionController);
//    }
//}
//
