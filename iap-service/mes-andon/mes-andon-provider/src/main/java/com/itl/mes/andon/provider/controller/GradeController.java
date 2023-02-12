package com.itl.mes.andon.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.service.GradeService;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 安灯等级
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@RestController
@RequestMapping("/grade")
@Api(tags = "安灯等级")
public class GradeController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private GradePushMapper gradePushMapper;

    /**
     * 信息
     */
    @ApiOperation(value = "根据id查询")
    @GetMapping("/{bo}")
    public ResponseData<Grade> info(@PathVariable("bo") String bo) {
        Grade grade = gradeService.getById(bo);
        return ResponseData.success(grade);
    }

    /**
     * 保存或更新
     */
    @ApiOperation(value = "保存或更新")
    @PostMapping("/saveOrUpdate")
    public ResponseData<Grade> saveOrUpdate(@RequestBody Grade grade) throws CommonException {
        gradeService.saveGrade(grade);
        return ResponseData.success();
    }

    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询安灯等级信息")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "GradeRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "andonGrade", value = "安灯等级编号，可不填"),
                            @DynamicParameter(name = "andonGradeName", value = "安灯等级名称，可不填"),
                            @DynamicParameter(name = "state", value = "是否启用，可不填")
                    }))
    public ResponseData<IPage<Grade>> page(@RequestBody Map<String, Object> params) {

        QueryWrapper<Grade> wrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(params.getOrDefault("andonGrade", "").toString())) {
            wrapper.like("ANDON_GRADE", params.get("andonGrade").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("andonGradeName", "").toString())) {
            wrapper.like("ANDON_GRADE_NAME", params.get("andonGradeName").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("state", "").toString())) {
            wrapper.eq("STATE", params.get("state").toString());
        }
        params.put("orderByField", "CREATE_DATE");
        params.put("isAsc", false);
        wrapper.lambda().eq(Grade::getSite, UserUtils.getSite());
        IPage<Grade> page = gradeService.page(new QueryPage<>(params), wrapper);
        return ResponseData.success(page);
    }


    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/pageByState")
    @ApiOperation(value = "分页查询可用的安灯等级信息")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "GradeRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "andonGrade", value = "安灯等级编号，可不填"),
                            @DynamicParameter(name = "andonGradeName", value = "安灯等级名称，可不填"),
                            @DynamicParameter(name = "state", value = "是否启用，可不填")
                    }))
    public ResponseData<IPage<Grade>> page2(@RequestBody Map<String, Object> params) {

        QueryWrapper<Grade> wrapper = new QueryWrapper<>();
        if (params.get("andonGrade") != null) {
            wrapper.like("ANDON_GRADE", params.get("andonGrade").toString());
        }
        if (params.get("andonGradeName") != null) {
            wrapper.like("ANDON_GRADE_NAME", params.get("andonGradeName").toString());
        }
        if (params.get("state") != null) {
            wrapper.eq("STATE", params.get("state").toString());
        }
        params.put("orderByField", "CREATE_DATE");
        params.put("isAsc", false);
        wrapper.lambda().eq(Grade::getState, "1");
        wrapper.lambda().eq(Grade::getSite, UserUtils.getSite());
        IPage<Grade> page = gradeService.page(new QueryPage<>(params), wrapper);
        return ResponseData.success(page);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public ResponseData<Grade> delete(@RequestBody String[] bos) {
        for (String bo : bos) {
            gradePushMapper.delete(new QueryWrapper<GradePush>().lambda().eq(GradePush::getAndonGradeBo, bo));
        }
        gradeService.removeByIds(Arrays.asList(bos));
        return ResponseData.success();
    }
}
