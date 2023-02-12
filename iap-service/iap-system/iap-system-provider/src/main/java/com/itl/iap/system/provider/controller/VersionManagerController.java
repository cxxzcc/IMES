//package com.itl.iap.system.provider.controller;
//
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.itl.iap.common.base.response.ResponseData;
//import com.itl.iap.common.base.utils.UserUtils;
//import com.itl.iap.system.api.entity.VersionController;
//import com.itl.iap.system.provider.service.impl.VersionControllerServiceImpl;
//import com.itl.iap.system.provider.utils.R;
//import io.swagger.annotations.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@Api("System-版本管理")
//@RestController
//@RequestMapping("/versionmanager")
//public class VersionManagerController {
//
//
//
//    @Autowired
//    private HttpServletRequest request;
//
//
//    @Autowired
//    private VersionControllerServiceImpl versionControllerService;
//
//
//
//    /**
//     * 根据传过来的版本号，与最新的版本号比对是否为最新app版本，来决定是否要更新APP
//     *
//     * @param versionNo APP版本号
//     * @param request      请求对象
//     * @return R
//     * @since 1.0
//     */
//    @ApiOperation(value = "检查是否为最新版本")
//    @GetMapping("/checkAppVersion")
//    public R<VersionController> checkAppVersion(@RequestParam("versionNo") String versionNo,
//                                                @RequestParam("type") Integer type,HttpServletRequest request) {
//        try {
//            return new R<>(versionControllerService.checkAppVersion(versionNo,type));
//        } catch (Exception e) {
//            return new R<>(R.FAIL, e.getMessage());
//        }
//    }
//
//    /**
//     *版本信息列表
//     *
//     * */
//
//    @PostMapping("/selectAppVersionsForTable")
//    @ApiOperation(value = "模糊查询版本ForTable")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "页面，默认为1"),
//            @ApiImplicitParam(name = "limit", value = "分页大小，默认20，可不填"),
//            @ApiImplicitParam(name = "orderBy主键ID", value = "排序属性，可不填"),
//            @ApiImplicitParam(name = "isAsc", value = "排序方式，true/false，可不填"),
//            @ApiImplicitParam(name = "versionNo", value = "版本号，可不填"),
//            @ApiImplicitParam(name = "downLoadLink", value = "下载链接，可不填"),
//            @ApiImplicitParam(name = "isUpToDate", value = "是否最新版本，可不填"),
//            @ApiImplicitParam(name = "remark", value = "备注"),
//            @ApiImplicitParam(name = "newContentLog", value = "版本新增内容日志"),
//            @ApiImplicitParam(name = "fixedContentLog", value = "版本修改内容日志"),
//            @ApiImplicitParam(name = "createdBy", value = "数据创建人"),
//            @ApiImplicitParam(name = "codeUrl"),
//            @ApiImplicitParam(name = "lastUpdateDate", value = "数据最后修改时间"),
//            @ApiImplicitParam(name = "creationDate", value = "数据创建时间"),
//            @ApiImplicitParam(name = "lastUpdatedBy", value = "数据最后修改人")
//
//    })
//    public ResponseData<IPage<Map<String, Object>>> selectPageVersionForTable(@RequestBody Map<String, Object> params) {
//
//        return ResponseData.success(versionControllerService.selectPageVersionForTable(new com.itl.iap.common.base.utils.QueryPage<>(params), params));
//    }
//
//    @ApiOperation(value = "添加版本")
//    @PostMapping("/addNewAppVersion")
//    public R addNewAppVersion(@RequestBody VersionController versionController, HttpServletRequest request) {
//        if (StrUtil.isBlank(versionController.getDownLoadLink())) {
//            return new R(R.FAIL, "请先上传apk!!!");
//        }
//        try {
//            versionControllerService.addNewAppVersion(versionController, UserUtils.getUserId());
//            return new R();
//        } catch (Exception e) {
//            return new R(R.FAIL, e.getMessage());
//        }
//    }
//
//    @ApiOperation(value = "设置最新版本")
//    @PostMapping("/setUpToDate")
//    public R setUpToDate(@RequestBody VersionController versionController, HttpServletRequest request) {
//        try {
//            versionControllerService.setUpToDate(versionController,UserUtils.getUserId());
//            return new R();
//        } catch (Exception e) {
//            return new R(R.FAIL, e.getMessage());
//        }
//    }
//
//
//}
