package com.itl.mom.form.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.form.provider.mapper.TableSchema;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tableSchema")
@Api(tags = "自定义表单表结构")
public class TableSchemaController {

    @Resource
    TableSchema tableSchema;

    /**
     * 查询自定义表单表信息
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询自定义表单表信息")
    public ResponseData<List<Map>> list(String id) {
        List<Map> tables = tableSchema.listTable();
        return ResponseData.success(tables);
    }
    /**
     * 查询自定义表单表字段信息
     */
    @PostMapping("/columns")
    @ApiOperation(value = "查询自定义表单表字段信息")
    public ResponseData<List<Map>> info(String tableName) {
        List<Map> tables = tableSchema.listTableColumn(tableName);
        return ResponseData.success(tables);
    }
}