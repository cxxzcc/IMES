package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.ErrorTypeDto;
import com.itl.iap.mes.api.entity.ErrorType;
import com.itl.iap.mes.api.service.ErrorTypeService;
import com.itl.iap.mes.provider.utils.SnowFlake;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yx
 * @date 2021/8/19
 * @since 1.8
 */
@Api("异常分类")
@RestController("/errorType")
public class ErrorTypeController {
    private ErrorTypeService errorTypeService;

    public ErrorTypeController(ErrorTypeService errorTypeService) {
        this.errorTypeService = errorTypeService;
    }

    @PostMapping("/page")
    @ApiOperation(value = "page")
    public ResponseData page(@RequestBody ErrorTypeDto errorTypeDto){
        List<ErrorTypeDto> ret = errorTypeService.listTree(errorTypeDto);
        return ResponseData.success(ret);
    }

    @PostMapping("/save")
    @ApiOperation(value = "save")
    public ResponseData save(@RequestParam(required = false, name = "parentId") String parentId,
                             @RequestBody ErrorType errorType){
        errorType.setParentId(parentId);
        errorType.setSite(UserUtils.getSite());
        if (StringUtils.isBlank(errorType.getId())) {
            errorType.setId(SnowFlake.getId().toString());
            return ResponseData.success(errorTypeService.save(errorType));
        }
        return ResponseData.success(errorTypeService.updateById(errorType));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "delete")
    public ResponseData delete(@RequestBody List<String> ids){
        errorTypeService.delete(ids);
        return ResponseData.success();
    }
}
