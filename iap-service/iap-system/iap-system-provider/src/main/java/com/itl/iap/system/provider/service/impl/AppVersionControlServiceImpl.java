package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.base.BaseEntity;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.system.api.constant.AppSystemTypeEnum;
import com.itl.iap.system.api.entity.AppVersionControl;
import com.itl.iap.system.provider.mapper.AppVersionControlMapper;
import com.itl.iap.system.provider.service.AppVersionControlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * APP版本控制业务层接口实现类
 *
 * @author yezi
 * @date 2019/5/29
 */
@Service
public class AppVersionControlServiceImpl extends ServiceImpl<AppVersionControlMapper, AppVersionControl> implements AppVersionControlService {

    @Resource
    private AppVersionControlMapper appVersionControlMapper;

    @Override
    public AppVersionControl checkAppVersion(String versionCode, String systemType) throws Exception {
        //获取对应版本号的app版本控制数据
        QueryWrapper<AppVersionControl> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(AppVersionControl::getVersionCode, versionCode)
                .eq(AppVersionControl::getSystemType, systemType);
        AppVersionControl selectVersion = this.getOne(queryWrapper);
        Assert.valid(selectVersion == null, "当前APP版本号:" + versionCode + "没有被维护,请联系系统管理员!!!");
        //如果当前版本不为最新，则返回最新版本app下载链接
        if (!"1".equals(selectVersion.getIsUpToDate())) {
            LambdaQueryWrapper<AppVersionControl> eq = Wrappers.<AppVersionControl>lambdaQuery()
                    .eq(AppVersionControl::getIsUpToDate, "1")
                    .eq(AppVersionControl::getSystemType, systemType);
            AppVersionControl latestVersion = this.getOne(eq);
            Assert.valid(latestVersion == null, "APP更新失败!!!原因为:系统没有维护最新的APP版本,请联系管理员!!!");
            String downloadUrl = latestVersion.getDownloadUrl();
            String lastestDownLoadLink = "";
            //安卓需要处理（全量更新 增量更新）
            //如果最新版本是增量
            if (AppSystemTypeEnum.ANDROID.getCode().equals(systemType) && downloadUrl.contains(".wgt")) {
                //获取当前版本之后的所有apk版本
                QueryWrapper<AppVersionControl> lastApk = new QueryWrapper<>();
                lastApk.lambda().likeLeft(AppVersionControl::getDownloadUrl, ".apk")
                        .eq(AppVersionControl::getSystemType, systemType)
                        .gt(AppVersionControl::getUpdateTime, selectVersion.getUpdateTime());
                lastApk.orderByDesc(BaseEntity.Fields.updateTime);
                List<AppVersionControl> apkList = this.list(lastApk);
                if (CollectionUtil.isNotEmpty(apkList)) {
                    lastestDownLoadLink = apkList.get(0).getDownloadUrl();
                } else {
                    lastestDownLoadLink = downloadUrl;
                }
            } else {
                lastestDownLoadLink = downloadUrl;
            }
            selectVersion.setLatestDownloadLink(lastestDownLoadLink);
        }
        return selectVersion;
    }

    @Override
    public void setAllRecordNotUpToDate(String systemType) {
        appVersionControlMapper.setAllRecordNotUpToDate(systemType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addNewAppVersion(AppVersionControl appVersionControl) {
        QueryWrapper<AppVersionControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppVersionControl::getVersionCode, appVersionControl.getVersionCode())
                .eq(AppVersionControl::getSystemType, appVersionControl.getSystemType());
        AppVersionControl one = this.getOne(queryWrapper);
        Assert.valid(one != null, "版本号已经存在！！");
        //设置所有记录不为最新版本
        this.setAllRecordNotUpToDate(appVersionControl.getSystemType());
        //默认为最新版本
        appVersionControl.setIsUpToDate("1");
        this.save(appVersionControl);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setUpToDate(AppVersionControl appVersionControl) {
        QueryWrapper<AppVersionControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(AppVersionControl::getId, appVersionControl.getId())
                .eq(AppVersionControl::getVersionCode, appVersionControl.getVersionCode())
                .eq(AppVersionControl::getSystemType, appVersionControl.getSystemType());
        AppVersionControl one = this.getOne(queryWrapper);
        Assert.valid(one != null, "版本号已经存在！！");
        this.setAllRecordNotUpToDate(appVersionControl.getSystemType());
        appVersionControl.setIsUpToDate("1");
        this.updateById(appVersionControl);
    }
}
