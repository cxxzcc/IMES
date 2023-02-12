package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import com.itl.mes.me.api.entity.ErrorType;
import com.itl.mes.me.api.service.ErrorTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 异常类型controller
 * @author dengou
 * @date 2021/11/4
 */
@RestController
@RequestMapping("/errortype")
@Api(tags = "异常类型")
public class ErrorTypeController {


    @Autowired
    private ErrorTypeService errorTypeService;

    /**
     * 查询异常树
     * @return 异常树列表
     * */
    @GetMapping("/tree")
    @ApiOperation(value = "查询异常树")
    public ResponseData<List<ErrorType>> getTree() {
        return ResponseData.success(errorTypeService.getTree());
    }

    /**
     * lov组件  post树
     * @return 异常树列表
     * */
    @PostMapping("/tree")
    @ApiOperation(value = "lov组件  post树")
    public ResponseData<List<ErrorType>> getTreeLov() {
        return ResponseData.success(errorTypeService.getLovTree());
    }


    /**
     * 新增节点
     * @param errorType 异常类型参数
     * @return 是否成功
     * */
    @PostMapping("/add")
    @ApiOperation(value = "新增异常, 不传parentId是新增顶级节点")
    public ResponseData<Boolean> add(@RequestBody @Validated(value = {ValidationGroupAdd.class}) ErrorType errorType) {
        errorType.setSite(UserUtils.getSite());
        return ResponseData.success(errorTypeService.add(errorType));
    }

    /**
     * 根据id查询异常类型详情
     * @param id 异常类型id
     * */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "查询异常类型详情")
    public ResponseData<ErrorType> getDetail(@PathVariable("id") String id) {
        return ResponseData.success(errorTypeService.detail(id));
    }

    /**
     * 修改节点
     * @param errorType 异常类型
     * @return 是否成功
     * */
    @PutMapping("/update")
    @ApiOperation(value = "修改异常类型")
    public ResponseData<Boolean> update(@RequestBody @Validated(value = {ValidationGroupUpdate.class}) ErrorType errorType) {
        return ResponseData.success(errorTypeService.updateErrorType(errorType));
    }

    /**
     * 删除节点
     * @param  id 异常类型id
     * @return 是否成功
     * */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除异常类型")
    public ResponseData<Boolean> deleteById(@PathVariable("id") String id) {
        return ResponseData.success(errorTypeService.delete(id));
    }




}
