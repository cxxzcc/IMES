package com.itl.mes.core.client.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.CodeRuleServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yx
 * @date 2021/4/7
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",
        contextId = "CodeRuleService",
        fallbackFactory = CodeRuleServiceImpl.class,
        configuration = FallBackConfig.class
)
@Component
public interface CodeRuleService {
    /**
     * 获取number个下一编码
     * @param codeGenerateDto
     * @return
     */
    @PostMapping("/codeRules/generatorNextNumbers")
    @ApiOperation("获取number个下一编码")
    ResponseData<List<String>> generatorNextNumberList(@RequestBody CodeGenerateDto codeGenerateDto);

    @GetMapping("/codeRules/getNewsNumberSI")
    String getNewsNumberSI(@RequestParam("codeRuleBo") String codeRuleBo) throws CommonException;


    /**
     * 根据编码类型生成获取单个编码
     * @param codeRuleType 编码规则类型
     * @return
     */
    @GetMapping("/codeRules/generatorNextNumber")
    @ApiOperation(value = "根据编码类型生成获取单个编码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codeRuleType", value = "编码规则类型", dataType = "string", paramType = "query"),
    })
    ResponseData<String> generatorNextNumber(@RequestParam("codeRuleType") String codeRuleType) throws CommonException;

}
