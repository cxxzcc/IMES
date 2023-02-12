package com.itl.mes.core.provider.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.mes.client.service.FileUploadService;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.TeamUserQueryDto;
import com.itl.mes.core.api.entity.Team;
import com.itl.mes.core.api.service.TeamService;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.api.vo.EmployeeVo;
import com.itl.mes.core.api.vo.TeamVo;
import com.itl.mes.core.provider.utils.excel.DeviceVoVerifyHandler;
import com.itl.mes.core.provider.utils.excel.TeamVoVerifyHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-06-25
 */
@RestController
@RequestMapping("/teams")
@Api(tags = " 班组信息主表" )
public class TeamController {
    private final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private HttpServletRequest request;

    @Value("${file.path}")
    private String filePath;

    @Autowired
    public TeamService teamService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private TeamVoVerifyHandler teamVoVerifyHandler;
    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Team> getTeamById(@PathVariable String id) {
        return ResponseData.success(teamService.getById(id));
    }



    @PostMapping("/save")
    @ApiOperation(value = "保存班组信息")
    public ResponseData<TeamVo> save(@RequestBody TeamVo teamVo) throws CommonException {
        teamService.save(teamVo);
        teamVo =  teamService.getTeamVoByTeam(teamVo.getTeam());
        return ResponseData.success(teamVo);
    }

    @ApiOperation(value = "export", notes = "导出模板", httpMethod = "GET")
    @GetMapping(value = "/export")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        teamService.export(request, response);
    }

    @ApiOperation(value = "importFile", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws IOException {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setTitleRows(0);
        params.setVerifyHandler(teamVoVerifyHandler);
        Map<String, Object> map = ExcelUtils.importExcel(
                file.getInputStream(),
                TeamVo.class,
                params,
                filePath,
                teamService::saveByImport,
                fileUploadService::uploadSingle
        );
        return ResponseData.success(map);
    }

    @ApiOperation(value = "download", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/download")
    public String download(HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = (new ClassPathResource("templates/team.xls")).getInputStream();
            out = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=班组维护导入模板.xls");
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
                    ;
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

    @GetMapping("/query")
    @ApiOperation(value = "查询班组信息")
    public ResponseData<TeamVo> getTeamVoByTeam(String team) throws CommonException {
        TeamVo groupVoByGrooup = teamService.getTeamVoByTeam(team);
        return ResponseData.success(groupVoByGrooup);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除班组信息")
    public ResponseData<String> delete(String team,String modifyDate) throws CommonException {
        teamService.delete(team, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }

    @GetMapping("/getEmployeeVo")
    @ApiOperation(value = "获取成员信息")
    public ResponseData<List<EmployeeVo>> getEmployeeVo(String employeeCode) throws CommonException {
        List<EmployeeVo> employeeVo = teamService.getEmployeeVo(employeeCode, null);
        return ResponseData.success(employeeVo);
    }

    @PostMapping("/getUsersByTeam")
    @ApiOperation(value = "根据班组查询用户列表")
    public ResponseData<Page<Map<String, Object>>> getUsersByTeam(@RequestBody TeamUserQueryDto params) {
        return ResponseData.success(teamService.getUsersByTeam(params));
    }
}
