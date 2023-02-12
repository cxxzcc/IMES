package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.client.service.CodeRuleService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yx
 * @date 2021/4/7
 * @since JDK1.8
 */
@Slf4j
@Service
public class CodeRuleServiceImpl implements CodeRuleService, FallbackFactory {
    @Override
    public Object create(Throwable throwable) {
        System.out.println(throwable.getMessage());
        return null;
    }

    @Override
    public ResponseData<List<String>> generatorNextNumberList(CodeGenerateDto codeGenerateDto){
        log.error("sorry CodeRuleService generatorNextNumberList feign fallback codeGenerateDto:{}" + codeGenerateDto.toString());
        return ResponseData.error("调用generatorNextNumberList失败");
    }

    @Override
    public String getNewsNumberSI(String codeRuleBo) throws CommonException {
        log.error("sorry CodeRuleService getNewsNumberSI feign fallback codeRuleBo: " + codeRuleBo);
        return null;
    }

    /**
     * 根据编码类型生成获取单个编码
     *
     * @param codeRuleType 编码规则类型
     * @return
     */
    @Override
    public ResponseData<String> generatorNextNumber(String codeRuleType) throws CommonException {
        log.error("sorry CodeRuleService generatorNextNumber feign fallback codeRuleType={}", codeRuleType);
        return ResponseData.error("调用generatorNextNumber失败");
    }

}
