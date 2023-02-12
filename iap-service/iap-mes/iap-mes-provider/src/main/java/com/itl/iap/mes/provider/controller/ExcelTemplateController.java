package com.itl.iap.mes.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.constants.ExcelTemplateEnum;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * excel模板controller
 * @author dengou
 * @date 2021/11/3
 */
@RequestMapping("/excel/template")
@RestController
public class ExcelTemplateController {


    @Value("${file.path}")
    private String filePath;

    /**
     * 获取下载地址
     * @param templateName 模板名称
     * @return 模板下载地址
     * */
    @GetMapping("/downloadUrl/{templateName}")
    public ResponseData<String> getDownLoadUrl(@PathVariable("templateName") String templateName) {
        String path = ExcelTemplateEnum.getPathByCode(templateName);
        Assert.valid(StrUtil.isBlank(path), "未找到指定excel模板");
        return ResponseData.success(filePath + path);
    }



}
