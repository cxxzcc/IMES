package com.itl.mom.label.provider.utils;

import cn.hutool.core.collection.CollUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchenghao
 * @date 2021/1/30 12:19
 */
@Component
public class CommonUtils {

    @Value("${ftp.url}")
    private String ftpUrl;


    public List<String> conversionUrl(List<String> urlList) throws CommonException {

        if (CollUtil.isEmpty(urlList)) {
            throw new CommonException("无法生成标签，请检查参数是否匹配，或有中文字符", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        List<String> newUrlList = new ArrayList<>();
        urlList.forEach(url -> {
            String[] listStr = url.split("/");
            String fileName = "/" + listStr[listStr.length-2] + "/" + listStr[listStr.length-1];
//            String fileName = url.substring(url.lastIndexOf("/") + 1);
            url = ftpUrl + fileName;
            newUrlList.add(url);
        });
        return newUrlList;
    }

    public String getFtpUrl() {
        return ftpUrl;
    }

}
