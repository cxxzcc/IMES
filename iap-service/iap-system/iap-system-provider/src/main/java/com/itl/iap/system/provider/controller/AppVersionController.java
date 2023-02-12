package com.itl.iap.system.provider.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.base.BaseEntity;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.system.api.dto.AppVersionControlDTO;
import com.itl.iap.system.api.entity.AppVersionControl;
import com.itl.iap.system.provider.service.AppVersionControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * APP版本控制-前端控制器
 *
 * @author yezi
 * @date 2019/5/29
 */
@RestController
@RequestMapping("/appVersionControl")
@Api(tags = "APP版本控制")
@Log4j2
@Validated
public class AppVersionController {

    @Autowired
    private AppVersionControlService appVersionControlService;

    /**
     * 根据传过来的版本号，与最新的版本号比对是否为最新app版本，来决定是否要更新APP
     *
     * @param versionCode APP版本号
     * @since 1.0
     */
    @ApiOperation(value = "检查版本", notes = "检查版本")
    @GetMapping("/checkAppVersion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "versionCode", value = "app版本号", required = true, paramType = "String"),
            @ApiImplicitParam(name = "systemType", value = "系统标识编码", required = true, paramType = "String")
    })
    public ResponseData<AppVersionControl> checkAppVersion(@RequestParam("versionCode") @NotBlank(message = "版本号不能为空") String versionCode,
                                                           @RequestParam("systemType") @NotBlank(message = "系统类型不能为空") String systemType) {
        try {
            return ResponseData.success(appVersionControlService.checkAppVersion(versionCode, systemType));
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 分页查询APP版本记录信息
     */
    @ApiOperation(value = "APP版本分页查询", notes = "APP版本分页查询")
    @GetMapping("/getAppVersionsByPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, paramType = "Integer"),
            @ApiImplicitParam(name = "systemType", value = "系统类型", required = false, paramType = "String")
    })
    public ResponseData<IPage<AppVersionControl>> getAppVersionsByPage(@RequestParam Integer page,
                                                                       @RequestParam Integer pageSize,
                                                                       @RequestParam String systemType) {
        try {
            Page pageInfo = new Page(page, pageSize);
            QueryWrapper<AppVersionControl> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().eq(StrUtil.isNotBlank(systemType), AppVersionControl::getSystemType, systemType);
            queryWrapper.orderByDesc(BaseEntity.Fields.updateTime);
            IPage<AppVersionControl> ipage = appVersionControlService.page(pageInfo, queryWrapper);
            for (AppVersionControl record : ipage.getRecords()) {
                record.setIsUpToDateName("1".equals(record.getIsUpToDate()) ? "是" : "否");
                record.setForceUpdateName("1".equals(record.getForceUpdate()) ? "是" : "否");
            }
            return ResponseData.success(ipage);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            return ResponseData.error(e.getMessage());
        }
    }

    @ApiOperation(value = "添加版本", notes = "添加版本")
    @PostMapping("/addNewAppVersion")
    public ResponseData addNewAppVersion(@RequestBody @Validated AppVersionControlDTO appVersionControlDTO) {
        try {
            Assert.valid(StrUtil.isBlank(appVersionControlDTO.getDownloadUrl()), "请先上传安装包!!!");
            AppVersionControl appVersionControl = BeanUtil.copyProperties(appVersionControlDTO, AppVersionControl.class);
            appVersionControlService.addNewAppVersion(appVersionControl);
            return ResponseData.success();
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            return ResponseData.error(e.getMessage());
        }
    }

    @ApiOperation(value = "设置最新版本", notes = "设置最新版本")
    @PostMapping("/setUpToDate")
    public ResponseData setUpToDate(@RequestBody @Validated AppVersionControlDTO appVersionControlDTO) {
        try {
            AppVersionControl appVersionControl = BeanUtil.copyProperties(appVersionControlDTO, AppVersionControl.class);
            appVersionControlService.setUpToDate(appVersionControl);
            return ResponseData.success();
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            return ResponseData.error(e.getMessage());
        }
    }

}
