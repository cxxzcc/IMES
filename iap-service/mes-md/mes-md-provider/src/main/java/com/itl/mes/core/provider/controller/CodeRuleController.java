package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.service.CodeRuleService;
import com.itl.mes.core.api.vo.CodeRuleFullVo;
import com.itl.mes.core.api.vo.CodeRuleItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author space
 * @since 2019-06-19
 */
@Slf4j
@RestController
@RequestMapping("/codeRules")
@Api(tags = "编码规则功能")
public class CodeRuleController {
    private final Logger logger = LoggerFactory.getLogger(CodeRuleController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public CodeRuleService codeRuleService;

    /**
     * 获取编号规则数据
     *
     * @param codeRuleType 编号类型，SN：车间作业控制，SO：工单，PK：包装
     */
    @GetMapping("/query")
    @ApiOperation("查询编号规则数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codeRuleType", value = "编号类型，SN：车间作业控制，SO：工单，PK：包装，OTHER：其它", dataType = "string", paramType = "query"),
    })
    public ResponseData<CodeRuleFullVo> getCodeRule(String codeRuleType) throws CommonException {

        return ResponseData.success(codeRuleService.getCodeRuleType(codeRuleType));
    }


    @PostMapping("/save")
    @ApiOperation("保存编号规则数据")
    public ResponseData<CodeRuleFullVo> saveCodeRule(@RequestBody CodeRuleFullVo codeRuleFullVo) throws CommonException {
        //初步验证数据格式
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(codeRuleFullVo);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (codeRuleFullVo.getCodeRuleItemVoList() == null || codeRuleFullVo.getCodeRuleItemVoList().size() == 0) {
            throw new CommonException("规则明细不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        for (CodeRuleItemVo codeRuleItemVo : codeRuleFullVo.getCodeRuleItemVoList()) {
            validResult = ValidationUtil.validateBean(codeRuleItemVo);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        codeRuleService.saveCodeRule(codeRuleFullVo);
        codeRuleFullVo = codeRuleService.getCodeRuleType(codeRuleFullVo.getCodeRuleType());
        return ResponseData.success(codeRuleFullVo);
    }


    /**
     * 删除编号规则类型数据
     *
     * @param codeRuleType 编码规则类型，SN：车间作业控制，SO：工单，PK：包装
     * @param type         ITEM/IG
     * @param code         编码
     * @param codeVersion  编码版本，当type为ITEM时，传入该值，其它不需要
     * @param modifyDate   修改时间
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除编码规则类型数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codeRuleType", value = "编码规则类型，SN：车间作业控制，SO：工单，PK：包装", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "定义方式：ITEM/IG", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "编码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "codeVersion", value = "编码版本，当type为ITEM时，传入该值，其它不需要", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "modifyDate", value = "修改时间", dataType = "string", paramType = "query")
    })
    public ResponseData<String> deleteCodeRuleTypeData(String codeRuleType, String type, String code, String codeVersion, String modifyDate) throws CommonException {
        if (StrUtil.isBlank(codeRuleType)) {
            throw new CommonException("编码规则类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StrUtil.isBlank(modifyDate)) {
            throw new CommonException("修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        codeRuleService.deleteCodeRuleTypeData(codeRuleType, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }

    @PostMapping("/generatorNextNumbers")
    @ApiOperation("获取number个下一编码")
    public ResponseData<List<String>> generatorNextNumberList(@RequestBody CodeGenerateDto codeGenerateDto) {
        try {
            return ResponseData.success(codeRuleService.generatorNextNumberList(codeGenerateDto.getCodeRuleBo(),
                    codeGenerateDto.getNumber(),
                    codeGenerateDto.getParamMap()));
        } catch (CommonException e) {
            e.printStackTrace();
            log.error("获取number个下一编码异常：{}", e);
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 根据编码类型生成获取单个编码
     *
     * @param codeRuleType 编码规则类型
     * @return
     * @throws CommonException
     */
    @GetMapping("/generatorNextNumber")
    @ApiOperation(value = "根据编码类型生成获取单个编码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codeRuleType", value = "编码规则类型", dataType = "string", paramType = "query"),
    })
    public ResponseData<String> generatorNextNumber(@RequestParam("codeRuleType") String codeRuleType) throws CommonException {
        if (StrUtil.isBlank(codeRuleType)) {
            throw new CommonException("编码规则类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String site = UserUtils.getSite();
        String codeRuleBo = new CodeRuleHandleBO(site, codeRuleType).getBo();
        return ResponseData.success(codeRuleService.generatorNextNumber(codeRuleBo));
    }

    /*
     * 获取领料单号
     */
    @GetMapping("/getNewsNumberSI")
    @ApiOperation(value = "领料单号生成")
    public String getNewsNumberSI(@RequestParam("codeRuleBo") String codeRuleBo) throws CommonException {
        return codeRuleService.getNewsNumberSI(codeRuleBo);
    }

}
