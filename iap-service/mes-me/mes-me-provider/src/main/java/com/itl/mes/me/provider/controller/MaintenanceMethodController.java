package com.itl.mes.me.provider.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import com.itl.iap.mes.client.service.FileUploadService;
import com.itl.mes.me.api.dto.MaintenanceMethodDto;
import com.itl.mes.me.api.entity.MaintenanceMethod;
import com.itl.mes.me.api.service.MaintenanceMethodService;
import com.itl.mes.me.api.vo.CorrectiveMaintenanceVo;
import com.itl.mes.me.provider.utils.excel.CorrectiveMaintenanceVerifyHander;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 维修方法controller
 *
 * @author dengou
 * @date 2021/11/4
 */
@RestController
@RequestMapping("/maintenanceMethod")
@Api(tags = "维修方法")
public class MaintenanceMethodController {

    @Autowired
    private MaintenanceMethodService maintenanceMethodService;

    @Autowired
    private CorrectiveMaintenanceVerifyHander correctiveMaintenanceVerifyHander;

    @Value("${file.path}")
    private String filePath;

    @Autowired
    private FileUploadService fileUploadService;


    /**
     * 分页列表
     *
     * @param params 查询参数
     * @return 分页查询参数
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页面，默认为1"),
            @ApiImplicitParam(name = "limit", value = "分页大小，默认20，可不填"),
            @ApiImplicitParam(name = "errorTypeCode", value = "异常类型code， 支持模糊查询"),
            @ApiImplicitParam(name = "errorTypeName", value = "异常类型名称, 支持模糊查询"),
            @ApiImplicitParam(name = "errorTypeDesc", value = "异常类型描述, 支持模糊查询"),
            @ApiImplicitParam(name = "code", value = "维修方法code, 支持模糊查询"),
            @ApiImplicitParam(name = "title", value = "维修方法名称/标题, 支持模糊查询"),
            @ApiImplicitParam(name = "description", value = "维修描述, 支持模糊查询"),
            @ApiImplicitParam(name = "method", value = "维修方法, 支持模糊查询"),
            @ApiImplicitParam(name = "isDisableFlag", value = "Y=启用, N=禁用")
    })
    public ResponseData<Page<MaintenanceMethodDto>> getPage(@RequestParam Map<String, Object> params) {
        StringUtils.replaceMatchTextByMap(params);
        return ResponseData.success(maintenanceMethodService.getPage(params));
    }

    /**
     * 分页列表-lov
     */
    @PostMapping("/lov/page")
    @ApiOperation(value = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页面，默认为1"),
            @ApiImplicitParam(name = "limit", value = "分页大小，默认20，可不填"),
            @ApiImplicitParam(name = "errorTypeCode", value = "异常类型code， 支持模糊查询"),
            @ApiImplicitParam(name = "errorTypeName", value = "异常类型名称, 支持模糊查询"),
            @ApiImplicitParam(name = "code", value = "维修方法code, 支持模糊查询"),
            @ApiImplicitParam(name = "title", value = "维修方法名称/标题, 支持模糊查询")
    })
    public ResponseData<Page<MaintenanceMethodDto>> getPageLov(@RequestBody Map<String, Object> params) {
        StringUtils.replaceMatchTextByMap(params);
        return ResponseData.success(maintenanceMethodService.getPage(params));
    }

    /**
     * 详情
     *
     * @param id 维修方法id
     * @return 维修方法详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "根据id查询维修方法详情")
    public ResponseData<MaintenanceMethodDto> getDetailById(@PathVariable("id") String id) {
        return ResponseData.success(maintenanceMethodService.getDetailById(id));
    }

    /**
     * 新增
     *
     * @param maintenanceMethod 维修方法详情
     * @return 是否成功
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public ResponseData<Boolean> add(@RequestBody @Validated(value = {ValidationGroupAdd.class}) MaintenanceMethod maintenanceMethod) {
        maintenanceMethod.setSite(UserUtils.getSite());
        return ResponseData.success(maintenanceMethodService.add(maintenanceMethod));
    }

    /**
     * 编辑
     *
     * @param maintenanceMethod 维修方法详情
     * @return 是否成功
     */
    @PutMapping("/update")
    @ApiOperation(value = "编辑")
    public ResponseData<Boolean> update(@RequestBody @Validated(value = {ValidationGroupUpdate.class}) MaintenanceMethod maintenanceMethod) {
        return ResponseData.success(maintenanceMethodService.updateMaintenance(maintenanceMethod));
    }

    /**
     * 删除
     *
     * @param ids id列表
     * @return 是否删除成功
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public ResponseData<Boolean> deleteBatchIds(@RequestBody List<String> ids) {
        return ResponseData.success(maintenanceMethodService.deleteBatchIds(ids));
    }

    /**
     * 导出
     * */

    /**
     * 导入
     */


    //下载模板
    @ApiOperation(value = "下载模板", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/download")
    public String download(HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = (new ClassPathResource("templates/maintenance.xls")).getInputStream();
            out = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
           response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode("维修措施模板.xls", "UTF-8")));


            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var15) {

                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var14) {
                }
            }
        }


        return "";
    }


    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "导入", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws IOException {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setTitleRows(0);
        params.setVerifyHandler(correctiveMaintenanceVerifyHander);
        Map<String, Object> map = ExcelUtils.importExcel(
                file.getInputStream(),
                CorrectiveMaintenanceVo.class,
                params,
                filePath,
                maintenanceMethodService::saveByImport,
                fileUploadService::uploadSingle
        );
        return ResponseData.success(map);
    }

    @ApiOperation(value = "导出", notes = "导出", httpMethod = "GET")
    @ApiImplicitParam(name="ids",value="ids",dataType="string", paramType = "query")
    @GetMapping(value = "/export")
    public void download(@RequestParam(required = false) String  ids, HttpServletResponse response) {
        maintenanceMethodService.export(ids, response);
    }




}


