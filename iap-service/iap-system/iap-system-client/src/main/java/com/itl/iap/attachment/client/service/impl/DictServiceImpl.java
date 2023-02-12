package com.itl.iap.attachment.client.service.impl;

import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.common.base.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 字典服务
 * @author dengou
 * @date 2021/10/13
 */
@Slf4j
@Component
public class DictServiceImpl implements DictService {
    @Override
    public ResponseData<Map<String, String>> getDictItemMapByParentCode(String code) {
        log.error("sorry DictService getDictItemMapByParentCode feign fallback" + code);
        return ResponseData.error("getDictItemMapByParentCode方法调用失败");
    }
}
