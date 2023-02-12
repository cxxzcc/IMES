package com.itl.mes.me.client.service.impl;

import com.itl.mes.me.api.entity.Action;
import com.itl.mes.me.client.service.ActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cch
 * @date 2021/6/1
 * @since JDK1.8
 */
@Component
@Slf4j
public class ActionServiceImpl implements ActionService {

    @Override
    public Action getById(String actionId) {
        log.error("sorry LabelPrintService checkBarCode feign fallback actionId:{}", actionId);
        return null;
    }
}
