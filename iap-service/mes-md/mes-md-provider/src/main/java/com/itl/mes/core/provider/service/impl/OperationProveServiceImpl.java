package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import com.itl.iap.mes.client.service.ProveService;
import com.itl.mes.core.api.dto.ProveDto;
import com.itl.mes.core.api.entity.OperationProve;
import com.itl.mes.core.api.service.OperationProveService;
import com.itl.mes.core.provider.mapper.OperationProveMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 工序-人员资质 服务实现类
 * </p>
 *
 * @author dengou
 * @since 2021-12-13
 */
@Service
public class OperationProveServiceImpl extends ServiceImpl<OperationProveMapper, OperationProve> implements OperationProveService {

    @Autowired
    private ProveService proveService;

    @Override
    public List<String> getProveIdListByOperation(String operationBo) {
        List<OperationProve> list = lambdaQuery().eq(OperationProve::getOperationBo, operationBo).list();
        if(CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(OperationProve::getProveId).filter(e-> StrUtil.isNotBlank(e)).collect(Collectors.toList());
    }

    @Override
    public Boolean save(String operationBo, List<OperationProve> operationProves) {
        List<OperationProve> list = lambdaQuery().eq(OperationProve::getOperationBo, operationBo).list();
        List<String> ids = list.stream().map(OperationProve::getId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(ids)) {
            removeByIds(ids);
        }
        if(CollUtil.isEmpty(operationProves)) {
            return false;
        }
        return saveBatch(operationProves);
    }

    @Override
    public List<ProveDto> getProveDtoListByOperationBo(String operationBo) {
        List<String> proveIdList = getProveIdListByOperation(operationBo);
        if(CollUtil.isNotEmpty(proveIdList)) {
            ResponseData<List<ProveEntity>> response = proveService.getByIds(proveIdList);
            if(response.isSuccess()) {
                List<ProveDto> proveDtos = new ArrayList<>();
                for (ProveEntity item : response.getData()) {
                    ProveDto proveDto = new ProveDto();
                    BeanUtils.copyProperties(item, proveDto);
                    proveDtos.add(proveDto);
                }
                return proveDtos;
            }
        }
        return Collections.emptyList();
    }
}
