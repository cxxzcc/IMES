package com.itl.iap.system.provider.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.dto.SysResourceMoveDTO;
import com.itl.iap.system.api.dto.SysResourceSortDTO;
import com.itl.iap.system.api.entity.IapSysAuthResourceT;
import com.itl.iap.system.api.entity.IapSysResourceT;
import com.itl.iap.system.api.service.IapSysAuthResourceTService;
import com.itl.iap.system.api.service.IapSysResourceTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单Controller
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Api("System-菜单管理控制层")
@RestController
@RequestMapping("/iapSysResourceT")
public class IapSysResourceTController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IapSysResourceTService iapSysResourceService;
    @Autowired
    private IapSysAuthResourceTService authResourceService;

    @GetMapping("/getAllMenuTwo")
    @ApiOperation(value = "获取当前用户菜单只要菜单", notes = "获取当前用户菜单只要菜单")
    public ResponseData getAllMenuTwo(@RequestParam(name = "clientType", required = false) String clientType) {
        logger.info("IapSysAuthResourceT getAllMenuTwo");
        return ResponseData.success(iapSysResourceService.getAllMenuTwo(clientType));
    }


    @PostMapping("/sourceMove")
    @ApiOperation(value = "菜单移动", notes = "菜单移动")
    public ResponseData sourceMove(@RequestBody SysResourceMoveDTO sysResourceMoveDTO) {
        logger.info("菜单移动...");
        String parentId = sysResourceMoveDTO.getParentId();
        List<String> idList = sysResourceMoveDTO.getIdList();
        Collection<IapSysResourceT> iapSysResourceTS = iapSysResourceService.listByIds(idList);
        List<IapSysResourceT> result = new ArrayList<>();
        for (IapSysResourceT iapSysResourceT : iapSysResourceTS) {
            if (0 == iapSysResourceT.getResourceType()) {
                IapSysResourceT source = new IapSysResourceT();
                source.setId(iapSysResourceT.getId());
                source.setParentId(parentId);
                result.add(source);
            }
        }
        if (CollectionUtil.isNotEmpty(result)) {
            iapSysResourceService.updateBatchById(result);
        }
        return ResponseData.success();
    }

    @PostMapping("/batchSort")
    @ApiOperation(value = "菜单调整排序", notes = "调整排序")
    public ResponseData batchSort(@RequestBody @NotEmpty List<SysResourceSortDTO> lists) {
        logger.info("菜单调整排序...");
        List<IapSysResourceT> result = new ArrayList<>();
        for (SysResourceSortDTO list : lists) {
            IapSysResourceT iapSysResourceT = new IapSysResourceT();
            iapSysResourceT.setId(list.getId());
            iapSysResourceT.setSort(list.getSort());
            result.add(iapSysResourceT);
        }
        iapSysResourceService.updateBatchById(result);
        return ResponseData.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增记录", notes = "新增记录")
    public ResponseData add(@RequestBody IapSysResourceT iapSysResourceT) {
        logger.info("IapSysResourceTDto add Record...");
        String id = iapSysResourceT.getId();
        if (id == null) {
            iapSysResourceT.setId(UUID.uuid32());
        }
        String resourcesCode = iapSysResourceT.getResourcesCode();
        String routerPath = iapSysResourceT.getRouterPath();
        QueryWrapper<IapSysResourceT> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IapSysResourceT::getResourcesCode, resourcesCode).or()
                .eq(IapSysResourceT::getRouterPath, routerPath);
        IapSysResourceT iapSysResourceT1 = iapSysResourceService.getOne(queryWrapper);
        if (iapSysResourceT1 != null && !iapSysResourceT1.getId().equals(id)) {
            throw new CommonException("编码或者url不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(iapSysResourceService.saveOrUpdate(iapSysResourceT));
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "根据ID删除记录", notes = "根据ID删除记录")
    public ResponseData delete(@RequestBody List<IapSysResourceTDto> iapSysResourceDto) {
        logger.info("IapSysResourceTDto delete Record...");
        return ResponseData.success(iapSysResourceService.deleteTree(iapSysResourceDto));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据ID修改记录", notes = "根据ID修改记录")
    public ResponseData update(@RequestBody IapSysResourceT iapSysResourceT) {
        logger.info("IapSysResourceTDto updateRecord...");
        return ResponseData.success(iapSysResourceService.updateById(iapSysResourceT));
    }

    @GetMapping("/queryList")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ResponseData queryRecord(@RequestParam(name = "clientType", required = false) String clientType) {
        logger.info("IapSysResourceTDto queryRecord...");
        return ResponseData.success(iapSysResourceService.queryList(clientType));
    }

    @PostMapping("/addSourceAll/{authId}")
    @ApiOperation(value = "权限列表菜单添加", notes = "权限列表菜单添加")
    public ResponseData addSourceAll(@RequestBody List<IapSysAuthResourceT> authResourceTs, @PathVariable("authId") String authId,
                                     @RequestParam(name = "clientType", required = false) String clientType) {
        logger.info("IapSysAuthResourceT saveBatch");
        return ResponseData.success(authResourceService.saveAuthResource(authResourceTs, authId, clientType));
    }

    @GetMapping("/selectSource")
    @ApiOperation(value = "权限列表菜单查询", notes = "权限列表菜单查询")
    public ResponseData selectSource(@RequestParam("authId") String authId, @RequestParam(value = "clientType", required = false) String clientType) {
        logger.info("IapSysAuthResourceT selectSource");
        return ResponseData.success(authResourceService.selectSource(authId, clientType));
    }

    @GetMapping("/getAllMenu")
    @ApiOperation(value = "获取当前用户菜单", notes = "获取当前用户菜单")
    public ResponseData getAllMenu(@RequestParam(name = "clientType", required = false) String clientType) {
        logger.info("IapSysAuthResourceT selectSource");
        return ResponseData.success(iapSysResourceService.getAllMenu(clientType));
    }

}
