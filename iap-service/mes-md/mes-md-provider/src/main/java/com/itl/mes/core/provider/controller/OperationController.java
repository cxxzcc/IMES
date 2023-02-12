package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.mes.core.api.dto.ProveDto;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.service.OperationProveService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.vo.OperationParam;
import com.itl.mes.core.api.vo.OperationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-05-28
 */
@RestController
@RequestMapping("/operations")
@Api(tags = " 工序表" )
public class OperationController {
    private final Logger logger = LoggerFactory.getLogger(OperationController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public OperationService operationService;
    @Autowired
    private OperationProveService operationProveService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Operation> getOperationById(@PathVariable String id) {
        return ResponseData.success(operationService.getById(id));
    }



    @PostMapping("/save")
    @ApiOperation(value = "保存工序数据")
    public ResponseData<OperationVo> saveByOperationVo(@RequestBody OperationVo operationVo) throws CommonException {
        if(operationVo==null) throw  new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        operationService.saveByOperationVo(operationVo);
        operationVo = operationService.getOperationVoByOperationAndVersion(operationVo.getOperation(), operationVo.getVersion());
        return ResponseData.success(operationVo);
    }
    @GetMapping("/query")
    @ApiOperation(value = "查询工序信息")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "operation", value = "工序", dataType = "string", paramType = "query" ),
            @ApiImplicitParam( name = "version", value = "版本", dataType = "string", paramType = "query" )
    })
    public ResponseData<OperationVo> selectByOperation(String operation,String version) throws CommonException {
        if(StrUtil.isBlank(operation)) throw new CommonException("工序编号参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        if(StrUtil.isBlank(version)) throw new CommonException("工序编号参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        OperationVo operationVoByOperationAndVersion = operationService.getOperationVoByOperationAndVersion(operation, version);
        return ResponseData.success(operationVoByOperationAndVersion);
    }





   @GetMapping("/delete")
   @ApiOperation(value = "删除工序信息")
    public ResponseData<String> delete(String operation,String version,String modifyDate) throws CommonException {
       if(StrUtil.isBlank(operation)) throw new CommonException("工序编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
       if(StrUtil.isBlank(modifyDate)) throw new CommonException("时间不能为空", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
       int delete = operationService.delete(operation,version,modifyDate);
       if(delete==0)throw new CommonException("工序编号不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
       return ResponseData.success("success");
    }

    @GetMapping("/processParam/{id}")
    @ApiOperation(value = "根据工序id查询工序参数")
    public ResponseData<List<OperationParam>> getOperationParamsById(@PathVariable("id") String id) {
        return ResponseData.success(operationService.getOperationParamsById(id));
    }



    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportItem(String site, HttpServletResponse response) {
        operationService.exportOperation(site, response);
    }


    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        //POIUtils.checkFile(file);
        List<OperationVo> operationVos = ExcelUtils.importExcel(file, 1, 1, OperationVo.class);
        for (OperationVo operationVo : operationVos) {
            operationService.saveByOperationVo(operationVo);
        }
        return ResponseData.success("success");
    }

    /**
     * feign接口  根据工序Id查询资质列表
     * */
    @GetMapping("/prove/{bo}")
    @ApiOperation(value = "根据工序Id查询资质列表")
    public ResponseData<List<ProveDto>> getProveDtoListByOperationBo(@PathVariable("bo") String operationBo) {
        return ResponseData.success(operationProveService.getProveDtoListByOperationBo(operationBo));
    }
}