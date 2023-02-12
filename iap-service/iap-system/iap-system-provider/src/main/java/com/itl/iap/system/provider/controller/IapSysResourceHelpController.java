package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.system.api.dto.IapSysResourceHelpDTO;
import com.itl.iap.system.api.entity.IapSysResourceHelp;
import com.itl.iap.system.api.entity.IapSysResourceT;
import com.itl.iap.system.api.service.IapSysResourceHelpService;
import com.itl.iap.system.api.service.IapSysResourceTService;
import com.itl.iap.system.provider.mapper.IapSysResourceHelpMapper;
import com.itl.mes.core.api.entity.CollectionRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author zhancen
 * @date 2021-11-10
 * @since jdk1.8
 */
@Api("System-帮助文档")
@RestController
@RequestMapping("/iapSysResourceHelp")
public class IapSysResourceHelpController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private IapSysResourceHelpService iapSysResourceHelpService;
    @Autowired
    private IapSysResourceHelpMapper iapSysResourceHelpMapper;

    @Autowired
    private IapSysResourceTService iapSysResourceTService;


    @PostMapping("/add")
    @ApiOperation(value = "新增/编辑记录", notes = "新增记录")
    public ResponseData add(@RequestBody IapSysResourceHelpDTO iapSysResourceHelpDTO) {
        logger.info("IapSysResourceTDto add Record...");
        IapSysResourceHelp iapSysResourceHelp = new IapSysResourceHelp();
        BeanUtils.copyProperties(iapSysResourceHelpDTO,iapSysResourceHelp);
        if (iapSysResourceHelpDTO.getId() != null) {
            return ResponseData.success(iapSysResourceHelpService.updateById(iapSysResourceHelp));
        } else {
            return ResponseData.success(iapSysResourceHelpService.saveOrUpdate(iapSysResourceHelp));
        }
    }
/*
    @GetMapping(value = "/getById/{id}")
    @ApiOperation(value = "getById", notes = "查询单条", httpMethod = "GET")
    public ResponseData getById(@PathVariable String id) {
        IapSysResourceHelp iapSysResourceHelp = iapSysResourceHelpService.getById(id);
        if(iapSysResourceHelp != null){
            IapSysResourceT iapSysResourceT = iapSysResourceTService.getById(iapSysResourceHelp.getResourceId());
            iapSysResourceHelp.setResourcesName(iapSysResourceT.getResourcesName());
            iapSysResourceHelp.setParentId(iapSysResourceT.getParentId());
            iapSysResourceHelp.setRouterPath(iapSysResourceT.getRouterPath());
        }
        return ResponseData.success(iapSysResourceHelp);
    }
*/

    @GetMapping("/getByUrl")
    @ApiOperation(value = "查询")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="routerPath", value = "产线 编码" ),

    })
    public ResponseData <Page<IapSysResourceHelp>> getByUrl(@RequestParam Map<String, Object> params) {
        return ResponseData.success(iapSysResourceHelpService.getByUrl(params));
    }

/**
    @GetMapping("/list")
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseData list() {
        List<IapSysResourceHelp> iapSysResourceHelpList = iapSysResourceHelpMapper.findList();
        return ResponseData.success(iapSysResourceHelpList);
    }
*/
}
