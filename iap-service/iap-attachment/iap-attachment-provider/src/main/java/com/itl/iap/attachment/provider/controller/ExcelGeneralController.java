package com.itl.iap.attachment.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itl.iap.attachment.api.entity.ExcelExportGeneral;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.attachment.api.service.ExcelGeneralService;
import com.itl.iap.attachment.api.service.IapUploadFileService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * description:
 * author: lK
 * time: 2021/7/6 9:48
 */
@Api("Excel通用导出与导入")
@RestController
@Slf4j
@RequestMapping("/GeneralExcel")
public class ExcelGeneralController {

    private ExcelGeneralService excelGeneral;

    @Autowired
    public void setExcelGeneral(ExcelGeneralService excelGeneral) {
        this.excelGeneral = excelGeneral;
    }

    private IapUploadFileService iapUploadFileService;

    @Autowired
    public void setIapUploadFileService(IapUploadFileService iapUploadFileService) {
        this.iapUploadFileService = iapUploadFileService;
    }

    /**
     * 工艺路线设置
     * 注解 @RequestPart("file") multipart/form-data表单提交请求
     * 上传excel模板,并构造sql插入excel_export_general表中
     * <p>
     * id->BusinessId 和前端约定好的枚举 产品:SetUpRouter-ITEM,产品组:SetUpRouter-GROUP,产线:SetUpRouter-LINE
     */
    @PostMapping("/upExcelTemplate")
    @ApiOperation(value = "工艺路线设置Excel模板上传专用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "上传的文件", paramType = "form"),
            @ApiImplicitParam(name = "id", value = "模板id", paramType = "form")
    })
    public ResponseData<String> upExcelTemplateRouter(@RequestPart("file") MultipartFile file, @RequestParam String id) {
        if (null == file) {
            throw new RuntimeException("文件不能为空");
        }

        // 删掉磁盘上原来的文件
        LambdaQueryWrapper<IapUploadFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IapUploadFile::getBusinessId, id);
        List<IapUploadFile> list = iapUploadFileService.list(queryWrapper);
        if (list != null && list.size() != 0) {
            iapUploadFileService.removeFile(list);
        }

        // 上传
        IapUploadFile iapUploadFile = iapUploadFileService.uploadFile(id, file);
        ExcelExportGeneral excelExportGeneral = new ExcelExportGeneral();
        excelExportGeneral.setId(id);
        excelExportGeneral.setCreateTime(new Date());
        excelExportGeneral.setUploadFileId(iapUploadFile.getId());
        excelExportGeneral.setStatus("0");
        excelExportGeneral.setDescribe("");
        excelExportGeneral.setSql("");
        excelExportGeneral.setUpdateTime(new Date());

        excelExportGeneral.setCreateUser(UserUtils.getUserName());
        excelExportGeneral.setUpdateUser(UserUtils.getUserName());
        excelExportGeneral.setSite(UserUtils.getSite());
        excelGeneral.saveOrUpdate(excelExportGeneral);

        return ResponseData.success(iapUploadFile.getFileUrl());
    }

    /**
     * 工艺路线设置
     * 通过id获取excel模板
     */
    @GetMapping("/export/excelTemplate")
    @ApiOperation(value = "根据模板id获取excel模板")
    public void getExcelTemplate(@RequestParam String id, HttpServletResponse response) throws CommonException {
        excelGeneral.exportExcelTemplate(id, response);
    }


    //-----------------------------------------------统一Excel模板上传----------------------------------------------------
    /**
     * excel导出统一上传模板
     * 上传excel模板,并构造sql插入excel_export_general表中
     */
//    @PostMapping("/upExcel")
//    @ApiOperation(value = "Excel模板上传")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "file", value = "上传的文件", paramType = "form"),
//            @ApiImplicitParam(name = "describe", value = "文件描述-前端先不用管", paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "status", value = "文件状态-前端先不用管", paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "businessId", value = "业务ID-前端先不用管", paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "sql", value = "查询sql-前端先不用管", paramType = "query", dataType = "String")
//    })
//    public ResponseData<String> upExcelTemplate(@RequestPart("file") MultipartFile file,
//                                                @RequestPart String describe,
//                                                @RequestPart String status,
//                                                @RequestPart String businessId,
//                                                @RequestPart String sql
//    ) {
//
//        if (null == file) {
//            throw new RuntimeException("文件不能为空");
//        }
//
//        IapUploadFile iapUploadFile = iapUploadFileService.uploadFile(businessId, file);
//
//        ExcelExportGeneral excelExportGeneral = new ExcelExportGeneral();
//        excelExportGeneral.setId(IdUtil.fastSimpleUUID());
//        excelExportGeneral.setCreateTime(new Date());
//        excelExportGeneral.setUploadFileId(iapUploadFile.getId());
//        excelExportGeneral.setStatus(status);
//        excelExportGeneral.setDescribe(describe);
//        excelExportGeneral.setSql(sql);
//        excelExportGeneral.setUpdateTime(new Date());
//
//        excelExportGeneral.setCreateUser(UserUtils.getUserName());
//        excelExportGeneral.setUpdateUser(UserUtils.getUserName());
//        excelExportGeneral.setSite(UserUtils.getSite());
//        excelGeneral.saveOrUpdate(excelExportGeneral);
//
//        return ResponseData.success(iapUploadFile.getFileUrl());
//    }

    /**
     * 通过id导出excel
     */
//    @GetMapping("/export")
//    @ApiOperation(value = "根据模板id导出excel")
//    public void getExcel(@RequestParam String id, HttpServletResponse response) throws CommonException {
//        excelGeneral.exportExcel(id, response);
//    }

    // todo
    //返回fastDFS中模板信息和该模板的sql表信息
//    @GetMapping("/getExportTemplate")
//    @ApiOperation(value = "查询excel模板")
//    public void getTemplate(@RequestParam String id, HttpServletResponse response) throws CommonException {
//
//    }

}
