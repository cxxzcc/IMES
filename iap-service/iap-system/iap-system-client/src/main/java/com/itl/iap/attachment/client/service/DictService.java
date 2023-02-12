package com.itl.iap.attachment.client.service;

import com.itl.iap.attachment.client.config.FallBackConfig;
import com.itl.iap.attachment.client.service.impl.DictServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 字典服务
 * @author dengou
 * @date 2021/10/13
 */
@FeignClient(value = "iap-system-provider", fallback = DictServiceImpl.class, configuration = FallBackConfig.class)
public interface DictService {

    /**
     * 根据字典编码code查询字典项map.  Key=字典项keyvalue;value=字典项名称
     * @param code 字典code
     * */
    @GetMapping("/iapDictItemT/itemMapByParentCode")
    ResponseData<Map<String, String>> getDictItemMapByParentCode(@RequestParam String code);

}
