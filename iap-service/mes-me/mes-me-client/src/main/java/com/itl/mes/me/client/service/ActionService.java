package com.itl.mes.me.client.service;

import com.itl.mes.me.api.entity.Action;
import com.itl.mes.me.client.config.FallBackConfig;
import com.itl.mes.me.client.service.impl.ActionServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author cch
 * @date 2021/6/1
 * @since JDK1.8
 */
@FeignClient(value = "mes-me-provider",contextId = "action", fallback = ActionServiceImpl.class, configuration = FallBackConfig.class)
public interface ActionService {
    @PostMapping("/action/getById/{actionId}")
    Action getById(@PathVariable("actionId") String actionId);
}
