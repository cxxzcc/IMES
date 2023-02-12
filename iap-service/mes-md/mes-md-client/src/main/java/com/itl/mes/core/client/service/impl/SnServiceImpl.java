package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.client.service.SnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author cch
 * @date 2021/4/27$
 * @since JDK1.8
 */
@Slf4j
@Component
public class SnServiceImpl implements SnService {
    @Override
    public ResponseData save(List<String> sns, String type) {
        log.error("sorry StationService selectByStation feign fallback station:{}" + sns + type);
        return null;
    }

    @Override
    public ResponseData check(String sn, String type) {
        log.error("sorry StationService selectByStation feign fallback station:{}" + sn + type);
        return null;
    }

    @Override
    public Sn getSnInfo(String sn) throws CommonException {
        log.error("sorry StationService selectByStation feign fallback station:{}" + sn);
        return null;
    }

}
