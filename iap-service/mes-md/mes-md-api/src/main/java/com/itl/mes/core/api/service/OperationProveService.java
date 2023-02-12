package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.ProveDto;
import com.itl.mes.core.api.entity.OperationProve;

import java.util.List;

/**
 * <p>
 * 工序-人员资质 服务类
 * </p>
 *
 * @author dengou
 * @since 2021-12-13
 */
public interface OperationProveService extends IService<OperationProve> {

    /**
     * 根据工序bo查询资质id列表
     * @param operationBo 工序bo
     * @return 资质id列表
     * */
    List<String> getProveIdListByOperation(String operationBo);

    /**
     * 覆盖保存指定工序的资质信息
     * @param operationBo 工序bo
     * @param operationProves 资质信息'
     * @return 是否成功
     * */
    Boolean save(String operationBo, List<OperationProve> operationProves);

    /**
     * 根据工序Bo查询资质列表
     * @param operationBo 工序bo
     * @return 资质列表
     * */
    List<ProveDto> getProveDtoListByOperationBo(String operationBo);

}
