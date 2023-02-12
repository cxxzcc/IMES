package com.itl.mes.core.provider.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.mes.core.api.dto.TemporaryDataRetryLogDTO;
import com.itl.mes.core.api.entity.TemporaryDataRetryLog;
import com.itl.mes.core.api.service.TemporaryDataRetryLogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  过站暂存数据重传记录 前端控制器
 * </p>
 *
 * @author dengou
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/temporary/retryLog")
public class TemporaryDataRetryLogController {


    @Autowired
    private TemporaryDataRetryLogService temporaryDataRetryLogService;

    /**
     * 分页查询重传记录列表
     * */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询重传记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="isRetryFlag", value = "是否重传" ),
            @ApiImplicitParam( name="sn", value = "条码，模糊查询" ),
            @ApiImplicitParam( name="shopOrder", value = "工单bo, 模糊查询" )
    })
    public ResponseData<Page<TemporaryDataRetryLogDTO>> getPage(@RequestParam Map<String, Object> params) {
        params.put("site", UserUtils.getSite());
        StringUtils.replaceMatchTextByMap(params);
        return ResponseData.success(temporaryDataRetryLogService.getPage(params));
    }


    /**
     * feign 保存重传记录
     * */
    @PostMapping("/save")
    @ApiOperation(value = "保存重传记录")
    public ResponseData<Boolean> saveLog(@RequestBody TemporaryDataRetryLog temporaryDataRetryLog) {
        return ResponseData.success(temporaryDataRetryLogService.temporaryDataRetryLog(temporaryDataRetryLog));
    }

}

