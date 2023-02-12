package com.itl.mes.core.client.service.impl;

import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.core.client.service.BomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yx
 * @date 2021/4/7
 * @since JDK1.8
 */
@Slf4j
@Service
public class BomServiceImpl implements BomService {
    @Override
    public BomVo selectByBo(String bo) {
        log.error("sorry BomService selectByBo feign fallback bo:{}" + bo);
        return null;
    }
}
