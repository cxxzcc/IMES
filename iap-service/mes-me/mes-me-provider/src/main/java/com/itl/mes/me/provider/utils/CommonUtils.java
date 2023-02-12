package com.itl.mes.me.provider.utils;

import cn.hutool.core.collection.CollUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
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


    public List<String> conversionUrl(ResponseData responseData) throws CommonException {

        List<String> urlList =(List<String>)responseData.getData();

        if (CollUtil.isEmpty(urlList)) {
            throw new CommonException("无法生成标签，请检查参数是否匹配，或有中文字符", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        List<String> newUrlList = new ArrayList<>();
        urlList.forEach(url ->{
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            url = ftpUrl+fileName;
            newUrlList.add(url);
        });
        return newUrlList;
    }

}
