package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import com.itl.iap.mes.client.service.ProveService;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.dto.ProveDto;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.Station;
import com.itl.mes.core.client.service.OperationService;
import com.itl.mes.core.client.service.StationService;
import com.itl.mes.me.api.service.ProductionExecutionCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生产执行判断服务实现
 * @author dengou
 * @date 2021/12/14
 */
@Service
public class ProductionExecutionCheckServiceImpl implements ProductionExecutionCheckService {

    @Autowired
    private StationService stationService;
    @Autowired
    private OperationService operationService;
    @Autowired
    private ProveService proveService;


    @Override
    public Boolean preCheck() {
        String userId = UserUtils.getUserId();
        String station = UserUtils.getStation();
        String site = UserUtils.getSite();
        ResponseData<Station> stationByIdResponse = stationService.getStationById(new StationHandleBO(site, station).getBo());
        Assert.valid(!stationByIdResponse.isSuccess(), stationByIdResponse.getMsg());
        Station stationEntity = stationByIdResponse.getData();
        Assert.valid(stationEntity == null, "未找到工位信息，" + station);
        //工序bo
        String operationBo = stationEntity.getOperationBo();
        //获取工序信息，判断是否开启资质判断
        ResponseData<Operation> operationByIdResponse = operationService.getOperationById(operationBo);
        Assert.valid(!operationByIdResponse.isSuccess(), operationByIdResponse.getMsg());
        Operation operation = operationByIdResponse.getData();
        String isCheckProveFlag = operation.getIsCheckProveFlag();
        if(StrUtil.isBlank(isCheckProveFlag) || StrUtil.equals(isCheckProveFlag, CommonConstants.FLAG_N)) {
            //不开启校验， 返回true
            return true;
        }

        //执行校验步骤
        //1. 获取工序所需资质列表
        ResponseData<List<ProveDto>> getProveDtoResponse = operationService.getProveDtoListByOperationBo(operationBo);
        Assert.valid(!getProveDtoResponse.isSuccess(), getProveDtoResponse.getMsg());
        List<ProveDto> proveDtoList = getProveDtoResponse.getData();
        //工序所需资质为空， 返回true
        if(CollUtil.isEmpty(proveDtoList)) {
            return true;
        }
        //查询工序所需资质是否关闭
        List<String> proveIds = proveDtoList.stream().map(ProveDto::getProveId).collect(Collectors.toList());
        ResponseData<List<ProveEntity>> proveListByIdsResult = proveService.getByIds(proveIds);
        Assert.valid(!proveListByIdsResult.isSuccess(), proveListByIdsResult.getMsg());
        List<ProveEntity> proveList = proveListByIdsResult.getData();
        //资质不存在 直接返回true
        if(CollUtil.isEmpty(proveList)) {
            return true;
        }

        //过滤已关闭的
        List<ProveEntity> inUseProveList = proveList.stream().filter(e -> CommonConstants.NUM_ZERO.equals(e.getState())).collect(Collectors.toList());
        //启用中的资质列表为空 直接返回true
        if(CollUtil.isEmpty(inUseProveList)) {
            return true;
        }

        //获取用户资质列表
        ResponseData<List<ProveEntity>> userProveResponse = proveService.getByUserId(userId, site);
        Assert.valid(!userProveResponse.isSuccess(), userProveResponse.getMsg());
        List<ProveEntity> userProveList = userProveResponse.getData();

        //判断用户是否有全部启用中资质， 是则返回true,否则返回缺少的资质
        List<String> userProveIdList = userProveList.stream().map(ProveEntity::getProveId).collect(Collectors.toList());
        List<ProveEntity> userNotHasProveList = new ArrayList<>();
        for (ProveEntity proveDto : inUseProveList) {
            if(userProveIdList.contains(proveDto.getProveId())) {
                continue;
            }
            userNotHasProveList.add(proveDto);
        }
        Assert.valid(CollUtil.isNotEmpty(userNotHasProveList), "该用户不具备工序所需的："+userNotHasProveList.stream().map(ProveEntity::getProveDescription).collect(Collectors.joining(",")) + "证明");
        return true;
    }
}
