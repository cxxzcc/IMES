package com.itl.iap.attachment.client.service.impl;

import com.itl.iap.attachment.client.service.IapOpsLogTService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author chenjx1
 * @date 2021-12-31
 * @since jdk 1.8
 */
@Slf4j
@Component
public class IapOpsLogTServiceImpl implements IapOpsLogTService, FallbackFactory {

    @Override
    public Object create(Throwable throwable) {
        System.out.println(throwable.getMessage());
        System.out.println(throwable.getStackTrace());
        return new IapOpsLogTServiceImpl();
    }

    @Override
    public ResponseData add(IapOpsLogT iapOpsLogT) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }

    @Override
    public ResponseData delete(IapOpsLogT iapOpsLogT) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }

    @Override
    public ResponseData update(IapOpsLogT iapOpsLogT) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }

    @Override
    public ResponseData queryRecord(IapOpsLogTDto iapOpsLogTDto) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }

    @Override
    public ResponseData pageQueryTypeInterface(IapOpsLogTDto iapOpsLogDto) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }

    @Override
    public ResponseData pageQueryException(IapOpsLogTDto iapOpsLogDto) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }

    @Override
    public ResponseData pageQueryInteractive(IapOpsLogTDto iapOpsLogDto) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }

    @Override
    public List<IapOpsLogT> listQueryInteractive(IapOpsLogT iapOpsLogT) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return null;
    }

    @Override
    public List<IapOpsLogT> getByIds(List<String> ids) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return null;
    }

    @Override
    public ResponseData call(List<String> ids) {
        log.error("sorry,IapOpsLogTService feign fallback ");
        return ResponseData.error("sorry,IapOpsLogTService feign fallback ");
    }
}
