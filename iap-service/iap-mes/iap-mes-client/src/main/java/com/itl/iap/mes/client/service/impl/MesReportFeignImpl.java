package com.itl.iap.mes.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanDTO;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanQueryDTO;
import com.itl.iap.mes.client.service.LabelService;
import com.itl.iap.mes.client.service.MesReportFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Slf4j
@Component
public class MesReportFeignImpl implements MesReportFeign, FallbackFactory {

    @Override
    public Object create(Throwable throwable) {
        System.out.println(throwable.getMessage());
        System.out.println(throwable.getStackTrace());
        return null;
    }

    @Override
    public ResponseData<List<CheckExecuteAndPlanDTO>> getCheckExecuteList(CheckExecuteAndPlanQueryDTO checkExecuteAndPlanQueryDTO) {
        System.out.println("调用失败");
        return null;
    }
}
