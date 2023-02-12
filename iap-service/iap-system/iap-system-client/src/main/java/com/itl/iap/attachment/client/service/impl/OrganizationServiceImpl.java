package com.itl.iap.attachment.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.excelDataEntity.SysOrganizationExcelEntity;
import com.itl.iap.attachment.client.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 组织服务远程调用
 *
 * @author 马家伦
 * @date 2020-8-25
 * @since jdk 1.8
 */
@Slf4j
@Component
public class OrganizationServiceImpl implements OrganizationService {
    @Override
    public ResponseData<List<SysOrganizationExcelEntity>> pageQuery(IapSysOrganizationTDto sysOrganizationDto) {
        log.error("sorry OrganizationService pageQuery feign fallback" + sysOrganizationDto.toString());
        return null;
    }

    @Override
    public ResponseData<Boolean> saveBatch(List<IapSysOrganizationT> sysOrganizationList) {
        log.error("sorry OrganizationService saveBatch feign fallback" + sysOrganizationList.toString());
        return null;
    }

    @Override
    public ResponseData<List<Map>> queryOrgPositionUserTreeList(String userOrPositOrOrganName) {
        log.error("sorry OrganizationService queryOrgPositionUserTreeList feign fallback" + userOrPositOrOrganName);
        return null;
    }

    @Override
    public ResponseData<List<IapSysOrganizationTDto>> queryOrgPositionUserByUserName(IapSysOrganizationTDto sysOrganizationDto) {
        log.error("sorry OrganizationService queryOrgPositionUserByUserName feign fallback" + sysOrganizationDto.toString());
        return null;
    }

    @Override
    public List<Map<String, String>> getByIds(Set<String> ids) {
        log.error("sorry OrganizationService getByIds feign fallback" + ids.toString());
        return null;
    }

    @Override
    public String getIdByCode(String code) {
        log.error("sorry OrganizationService getIdByCode feign fallback" + code);
        return null;
    }
}
