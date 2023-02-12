package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;
import com.itl.iap.system.api.service.IapOpsLogTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交互接口日志Controller
 *
 * @author chenjx1
 * @date 2021-12-31
 * @since jdk1.8
 */
@Api("System-交互接口日志控制层")
@RestController
@RequestMapping("/iapOpsLogT")
public class IapOpsLogTController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IapOpsLogTService iapOpsLogService;

    @PostMapping("/add")
    @ApiOperation(value = "新增记录", notes = "新增登陆记录")
    public ResponseData add(@RequestBody IapOpsLogT iapOpsLogT) {
        logger.info("IapOpsLogT add Record...");
        return ResponseData.success(iapOpsLogService.saveOrUpdate(iapOpsLogT));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据日志ID删除记录", notes = "根据日志ID删除记录")
    public ResponseData delete(@RequestBody IapOpsLogT iapOpsLogT) {
        logger.info("IapOpsLogT delete Record...");
        return ResponseData.success(iapOpsLogService.removeById(iapOpsLogT.getId()));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据日志ID修改记录", notes = "根据日志ID修改记录")
    public ResponseData update(@RequestBody IapOpsLogT iapOpsLogT) {
        logger.info("IapOpsLogT updateRecord...");
        return ResponseData.success(iapOpsLogService.updateById(iapOpsLogT));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询全部Ops日志", notes = "分页查询全部Ops日志")
    public ResponseData queryRecord(@RequestBody IapOpsLogTDto iapOpsLogTDto) {
        logger.info("IapOpsLogTDto queryRecord...");
        return ResponseData.success(iapOpsLogService.pageQuery(iapOpsLogTDto));
    }

    @PostMapping("/pageQueryTypeInterface")
    @ApiOperation(value = "分页查询系统接口日志", notes = "分页查询系统接口日志")
    public ResponseData pageQueryTypeInterface(@RequestBody IapOpsLogTDto iapOpsLogDto) {
        logger.info("IapOpsLogTDto queryRecord...");
        return ResponseData.success(iapOpsLogService.pageQueryTypeInterface(iapOpsLogDto));
    }

    @PostMapping("/pageQueryException")
    @ApiOperation(value = "分页查询异常日志", notes = "分页查询异常日志")
    public ResponseData pageQueryException(@RequestBody IapOpsLogTDto iapOpsLogDto) {
        logger.info("IapOpsLogTDto queryRecord...");
        return ResponseData.success(iapOpsLogService.pageQueryException(iapOpsLogDto));
    }

    @PostMapping("/pageQueryInteractive")
    @ApiOperation(value = "分页查询交互接口日志", notes = "分页查询交互接口日志")
    public ResponseData pageQueryInteractive(@RequestBody IapOpsLogTDto iapOpsLogDto) {
        logger.info("IapOpsLogTDto queryRecord...");
        return ResponseData.success(iapOpsLogService.pageQueryInteractive(iapOpsLogDto));
    }

    @PostMapping("/listQueryInteractive")
    @ApiOperation(value = "交互接口日志List", notes = "交互接口日志List")
    public List<IapOpsLogT> listQueryInteractive(@RequestBody IapOpsLogT iapOpsLogT) {
        logger.info("IapOpsLogT queryRecord...");
        return iapOpsLogService.listQueryInteractive(iapOpsLogT);
    }

    @PostMapping("/getByIds")
    @ApiOperation(value = "交互接口日志List", notes = "交互接口日志List")
    public List<IapOpsLogT> getByIds(@RequestParam List<String> ids) {
        logger.info("ids queryRecord...");
        return iapOpsLogService.getByIds(ids);
    }

    @ApiOperation("交互接口推送")
    @PostMapping("/call")
    public ResponseData call(@RequestBody List<String> ids) {
        return iapOpsLogService.call(ids);
    }

}
