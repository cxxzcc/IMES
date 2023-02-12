package com.itl.mom.label.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.LabelTypeQueryDTO;
import com.itl.mom.label.api.entity.label.LabelTypeEntity;
import com.itl.mom.label.provider.impl.label.LabelTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/4 11:11
 */
@Api(tags = "标签类型管理")
@RestController
@RequestMapping("/sys/labelType")
public class LabelTypeController {


    @Autowired
    LabelTypeServiceImpl labelTypeService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody LabelTypeQueryDTO labelTypeQueryDTO) {
        return ResponseData.success(labelTypeService.findList(labelTypeQueryDTO));
    }

    @GetMapping("/queryToSelect")
    @ApiOperation(value = "下拉框选项查询", httpMethod = "GET")
    public ResponseData queryToSelect(){
        return ResponseData.success(labelTypeService.queryToSelect());
    }

    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询信息ByState", notes = "分页查询信息")
    public ResponseData findListByState(@RequestBody LabelTypeQueryDTO labelTypeQueryDTO) {
        return ResponseData.success(labelTypeService.findListByState(labelTypeQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody LabelTypeEntity labelTypeEntity) throws CommonException {
        labelTypeService.save(labelTypeEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        ResponseData data = labelTypeService.delete(ids);
        return data;
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(labelTypeService.findById(id));
    }

    @ApiOperation(value = "getByIdList", notes = "查看一条", httpMethod = "GET")
    @PostMapping(value = "/getByIdList")
    public ResponseData<List<LabelTypeEntity>> getByIdList(@RequestBody List<String> idList) {
        return ResponseData.success(labelTypeService.getByIdList(idList));
    }

}
