package com.itl.mom.label.provider.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.dto.UpdateDoneDto;
import com.itl.mom.label.api.bo.SnHandleBO;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.api.service.MeProductStatusService;
import com.itl.mom.label.api.vo.MeProductStatusQueryVo;
import com.itl.mom.label.provider.mapper.MeProductStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * product_status(MeProductStatus)表服务实现类
 *
 * @author makejava
 * @since 2021-10-22 11:09:55
 */
@Service("meProductStatusService")
@RequiredArgsConstructor
public class MeProductStatusServiceImpl extends ServiceImpl<MeProductStatusMapper, MeProductStatus> implements MeProductStatusService {


    private final MeProductStatusMapper meProductStatusMapper;

    @Override
    public Boolean closeBySnboList(List<String> snboList) {
        if (CollUtil.isEmpty(snboList)) {
            return false;
        }
        MeProductStatus meProductStatus = new MeProductStatus();
        meProductStatus.setState(0);

        UpdateWrapper<MeProductStatus> wrapper = new UpdateWrapper<MeProductStatus>()
                .in("sn_bo", snboList)
                .ne("state", 0);
        return update(meProductStatus, wrapper);
    }

    @Override
    public MeProductStatus getBySn(String sn, String site) {
        String snBo = new SnHandleBO(site, sn).getBo();
        return getBySnBo(snBo);
    }

    /**
     * 根据snBo查询
     * */
    private MeProductStatus getBySnBo(String snBo) {
        return lambdaQuery()
                .ne(MeProductStatus::getState, CommonConstants.NUM_ZERO)
                .eq(MeProductStatus::getSnBo, snBo).one();
    }

    @Override
    public List<MeProductStatus> getBySnBos(List<String> snBos) {
        if(CollUtil.isEmpty(snBos)) {
            return Collections.emptyList();
        }
        return baseMapper.getBySnBos(snBos);
    }

    /**
     * @param sn     sn
     * @param status 状态 1 正常 0 关闭
     * @return
     */
    @Override
    public ResponseData<List<MeProductStatusQueryVo>> findProductStatusBySnAndStatus(String sn, int status) {

        List<MeProductStatusQueryVo> meProductStatusQueryVos = meProductStatusMapper.findProductStatusBySnAndStatus(sn, status);
        return ResponseData.success(meProductStatusQueryVos);
    }

    /**
     * @param productStateUpdateDtos productStateUpdateDto
     * @return ResponseData.class
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public ResponseData<Boolean> productStateUpdate(List<ProductStateUpdateDto> productStateUpdateDtos) {
        //通过bo获取唯一的数据
        productStateUpdateDtos.forEach(productStateUpdateDto->{
            String productStateBo = productStateUpdateDto.getProductStateBo();
            LambdaQueryWrapper<MeProductStatus> query = new QueryWrapper<MeProductStatus>().lambda()
                    .eq(MeProductStatus::getId, productStateBo);
            MeProductStatus meProductStatus = meProductStatusMapper.selectOne(query);
            //修改值
            meProductStatus.setOnline(productStateUpdateDto.getOnLine())
                    .setCurrentD(productStateUpdateDto.getDate())
                    .setCurrentOperation(productStateUpdateDto.getOperationName())
                  //  .setNextOperation(productStateUpdateDto.getNextOperationName())
                    .setCurrentPlStation(productStateUpdateDto.getStationsStationAndPLName())
                    .setCurrentOperationId(productStateUpdateDto.getOperationBo())
                  //  .setNextOperationId(productStateUpdateDto.getNextOperationBo())
                    .setCurrentPerson(productStateUpdateDto.getOperationUserName());
             meProductStatusMapper.updateProductState(meProductStatus);
        });


        return ResponseData.success();
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public ResponseData updateProductStatusDoneByBo(List<UpdateDoneDto> updateDoneDtos) {
        updateDoneDtos.forEach(x->{
            meProductStatusMapper.updateProductStatusDoneByBo(x.getProductStateBo(), x.getDone());
        });
            return ResponseData.success();
    }

    @Override
    public Boolean updateNextProcess(ProductStateUpdateDto productStateUpdateDtos) {
        List<MeProductStatus> meProductStatuses = getBySnBos(Arrays.asList(productStateUpdateDtos.getSnBo()));
        if(CollUtil.isEmpty(meProductStatuses)) {
            return false;
        }
        MeProductStatus meProductStatus = meProductStatuses.get(0);
        MeProductStatus updateMeProductStatus = new MeProductStatus();
        updateMeProductStatus.setId(meProductStatus.getId());
        updateMeProductStatus.setNextOperationId(productStateUpdateDtos.getNextOperationBo());
        updateMeProductStatus.setNextOperation(productStateUpdateDtos.getNextOperationName());
        updateMeProductStatus.setDone(productStateUpdateDtos.getDone());

        return updateById(updateMeProductStatus);
    }

    @Override
    public Boolean updateNextProcessBatch(List<String> snBos, String nextProcessId, String nextProcessName) {
        List<MeProductStatus> meProductStatuses = getBySnBos(snBos);
        if(CollUtil.isEmpty(meProductStatuses)) {
            return false;
        }

        List<MeProductStatus> updateList = new ArrayList<>();
        for (MeProductStatus meProductStatus : meProductStatuses) {
            MeProductStatus updateMeProductStatus = new MeProductStatus();
            updateMeProductStatus.setId(meProductStatus.getId());
            updateMeProductStatus.setNextOperationId(nextProcessId);
            updateMeProductStatus.setNextOperation(nextProcessName);
            updateList.add(updateMeProductStatus);
        }
        return updateBatchById(updateList);
    }

    @Override
    public Boolean updateIsHoldByIds(List<String> ids, Integer isHold, Boolean checkSnClosed) {
        if(CollUtil.isEmpty(ids)) {
            return false;
        }
        if(checkSnClosed) {
            Collection<MeProductStatus> meProductStatuses = listByIds(ids);
            Assert.valid(CollUtil.isEmpty(meProductStatuses), "未找到数据");
            //已关闭条码bo列表
            List<String> closeSnBoList = meProductStatuses.stream().filter(e -> CommonConstants.NUM_ZERO.equals(e.getState())).map(MeProductStatus::getSnBo).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(closeSnBoList)) {
                List<String> closeSnList = new ArrayList<>();
                closeSnBoList.forEach(e->{
                    closeSnList.add(new SnHandleBO(e).getSn());
                });
                Assert.valid(true, "条码:" + CollUtil.join(closeSnList, StrUtil.COMMA) + "已关闭");
            }
        }

        MeProductStatus meProductStatus = new MeProductStatus();
        meProductStatus.setHold(isHold);
        UpdateWrapper<MeProductStatus> wrapper = new UpdateWrapper<>();
        wrapper.in("id", ids);
        return update(meProductStatus, wrapper);
    }

    @Override
    public Boolean updateIsHoldBySnList(List<String> snBoList, Integer isHold) {
        List<MeProductStatus> meProductStatuses = getBySnBos(snBoList);
        if(CollUtil.isNotEmpty(meProductStatuses)) {
            List<String> ids = meProductStatuses.stream().map(MeProductStatus::getId).collect(Collectors.toList());
            return updateIsHoldByIds(ids, isHold, false);
        }
        return false;
    }

    @Override
    public List<MeProductStatus> getShopOrderBySnBoList(List<String> snBoList) {
        if(CollUtil.isEmpty(snBoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getShopOrderBySnBoList(snBoList);
    }

    /**
     * 重写save方法，保证每个sn+状态为正常的只存在一条数据
     * */
    @Override
    public boolean save(MeProductStatus entity) {
        if(entity == null) {
            return false;
        }
        MeProductStatus meProductStatus = getBySnBo(entity.getSnBo());
        if(meProductStatus == null) {
            return super.save(entity);
        }
        entity.setId(meProductStatus.getId());
        return updateById(entity);
    }

    /**
     * 重写saveBatch方法，保证每个sn+状态为正常的只存在一条数据
     * */
    @Override
    public boolean saveBatch(Collection<MeProductStatus> entityList) {
        if(CollUtil.isEmpty(entityList)) {
            return false;
        }
        List<String> snBos = entityList.stream().map(MeProductStatus::getSnBo).collect(Collectors.toList());
        List<MeProductStatus> list = getBySnBos(snBos);
        if(CollUtil.isNotEmpty(list)) {
            Map<String, String> snBoIdMap = list.stream().collect(Collectors.toMap(MeProductStatus::getSnBo, MeProductStatus::getId));
            entityList.forEach(e-> {
                e.setId(snBoIdMap.get(e.getSnBo()));
            });
        }
        return saveOrUpdateBatch(entityList);
    }
}
