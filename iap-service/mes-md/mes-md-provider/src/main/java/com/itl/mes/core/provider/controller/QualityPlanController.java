package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.entity.QualityPlan;
import com.itl.mes.core.api.service.CustomDataService;
import com.itl.mes.core.api.service.QualityPlanService;
import com.itl.mes.core.api.vo.CustomDataVo;
import com.itl.mes.core.api.vo.QualityPlanAtParameterVo;
import io.swagger.annotations.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 *
 * @author lzh
 * @since 2019-08-29
 */
@RestController
@RequestMapping("/monopy/qualityPlans")
@Api(tags = "质量控制计划维护")
public class QualityPlanController {
    private final Logger logger = LoggerFactory.getLogger(QualityPlanController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public QualityPlanService qualityPlanService;

    @Autowired
    private CustomDataService customDataService;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    public ResponseData<QualityPlan> getQualityPlanById(@PathVariable String id) {
        return ResponseData.success(qualityPlanService.getById(id));
    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    @PostMapping("/selectQualityPlanPage")
    @ApiOperation(value = "分页查询质量控制计划")
    @ApiOperationSupport(params = @DynamicParameters(name = "InspectTypeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不传"),
            @DynamicParameter(name = "isCurrentVersion", value = "是否当前版本，可不传"),
            @DynamicParameter(name = "version", value = "版本，可不传"),
            @DynamicParameter(name = "qualityPlan", value = "质量控制编号，可不传"),
            @DynamicParameter(name = "qualityPlanDesc", value = "返回字段：质量控制编号描述，可不传"),
            @DynamicParameter(name = "QUALITY_PLAN", value = "返回字段：质量控制编号，不需要传入"),
            @DynamicParameter(name = "QUALITY_PLAN_DESC", value = "返回字段：质量控制编号描述，不需要传入"),
            @DynamicParameter(name = "VERSION", value = "返回字段：版本，不需要传入"),
            @DynamicParameter(name = "IS_CURRENT_VERSION", value = "返回字段：是否当前版本，不需要传入"),
    }))
    public ResponseData<IPage<Map>> selectQualityPlanPage(@RequestBody Map<String, Object> params) {

        return ResponseData.success(qualityPlanService.selectQualityPlanPage(new QueryPage<>(params), params));
    }

    /**
     * 获取自定义数据
     * @return
     */
    @GetMapping("/getQualityPlanCustomData")
    @ApiOperation(value = "获取质量控制计划维护自定义数据")
    public ResponseData<List<CustomDataVo>> getQualityPlanCustomData() {
        return ResponseData.success(customDataService.selectCustomDataVoListByDataType(CustomDataTypeEnum.QUALITY_PLAN.getDataType()));
    }

    /**
     * 获取质量控制编号
     * @return
     */
    @GetMapping("/getNewsNumber")
    @ApiOperation(value = "创建质量控制计划编号")
    public ResponseData<String> getNewsNumber() throws CommonException {
        return ResponseData.success(qualityPlanService.getQPnumber());
    }

    /**
     * 保存或新增控制质量数据
     *
     * @param qppVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存或新增控制质量数据")
    public ResponseData<QualityPlan> saveInUpdate(@RequestBody QualityPlanAtParameterVo qppVo) throws CommonException {
        if (qppVo == null) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StrUtil.isBlank(qppVo.getQualityPlan())) {
            throw new CommonException("控制质量编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        QualityPlan qualityPlan= qualityPlanService.saveInUpdate(qppVo);
        return ResponseData.success(qualityPlan);
    }

    /**
     * 精确查询
     * @param qualityPlan
     * @param version
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询控制质量数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "qualityPlan", value = "检验类型编号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "string", paramType = "query"),
    })
    public ResponseData<QualityPlanAtParameterVo> getQpapVoByQualityPlan(String qualityPlan,String version) throws CommonException {
        QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        return ResponseData.success(qualityPlanAtParameterVo);
    }

    /**
     * 删除控制质量数据
     * @param qualityPlan
     * @param version
     * @param modifyDate
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除控制质量数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "qualityPlan", value = "检验类型编号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "modifyDate", value = "修改日期", dataType = "string", paramType = "query")
    })
    public ResponseData deleteQuality(String qualityPlan,String version, String modifyDate) throws CommonException {
        if (qualityPlan == null || StrUtil.isBlank(qualityPlan)) {
            throw new CommonException("检验类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (version == null || StrUtil.isBlank(version)) {
            throw new CommonException("版本不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (modifyDate == null || StrUtil.isBlank(modifyDate)) {
            throw new CommonException("时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        qualityPlanService.deleteQuality(qualityPlan,version, DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ));
        return ResponseData.success("success");
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportQplan(String site, HttpServletResponse response) {

        try {
            qualityPlanService.exportQplan(site, response);
        } catch (CommonException e) {
            logger.error("exportQplan -=- {}", ExceptionUtils.getFullStackTrace(e));
        }
    }

    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        qualityPlanService.importExcel(file);
        return ResponseData.success("success");
    }

}