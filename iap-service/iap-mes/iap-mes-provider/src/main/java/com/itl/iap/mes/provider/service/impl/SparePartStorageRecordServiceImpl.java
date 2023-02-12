package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.common.base.constants.SystemDictCodeConstant;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.SparePartChangeRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageDetailRequestDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageRequestDTO;
import com.itl.iap.mes.api.dto.sparepart.SparepartStorageCountStatisticsDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePartInventory;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecord;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecordDetail;
import com.itl.iap.mes.api.service.SparePartInventoryService;
import com.itl.iap.mes.api.service.SparePartStorageRecordDetailService;
import com.itl.iap.mes.api.service.SparePartStorageRecordService;
import com.itl.iap.mes.provider.feign.CustomerFeignService;
import com.itl.iap.mes.provider.feign.SupplierFeignService;
import com.itl.iap.mes.provider.mapper.SparePartStorageRecordMapper;
import com.itl.mes.core.client.service.CodeRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 备件出入库记录服务实现
 * @author dengou
 * @date 2021/9/22
 */
@Service
public class SparePartStorageRecordServiceImpl extends ServiceImpl<SparePartStorageRecordMapper, SparePartStorageRecord> implements SparePartStorageRecordService {

    @Autowired
    private SparePartStorageRecordDetailService sparePartStorageRecordDetailService;
    @Autowired
    private SparePartInventoryService sparePartInventoryService;
    @Autowired
    private CodeRuleService codeRuleService;
    @Autowired
    private CustomerFeignService customerFeignService;
    @Autowired
    private DictService dictService;
    @Autowired
    private SupplierFeignService supplierFeignService;

    @Override
    public Page<SparePartStorageRecordDTO> getStorageRecordBySparePart(Map<String, Object> params) {
        QueryPage<SparePartStorageRecordDTO> queryPage = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        List<SparePartStorageRecordDTO> list = baseMapper.getStorageRecordBySparePart(queryPage, params);
        if(CollUtil.isNotEmpty(list)) {
            List<String> ids = list.stream().map(SparePartStorageRecordDTO::getCustomer).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(ids)) {
                ResponseData<List<Map<String, Object>>> result = customerFeignService.getByIds(ids);
                if(StrUtil.equals(result.getCode(), ResultResponseEnum.SUCCESS.getCode())) {
                    List<Map<String, Object>> data = result.getData();
                    Map<String, String> map = data.stream().collect(Collectors.toMap(e -> (String) e.get("id"), e -> (String) e.get("customerName")));
                    list.forEach(e -> {
                        if(map.containsKey(e.getCustomer())) {
                            e.setCustomerName(map.get(e.getCustomer()));
                        }
                    });
                }
            }
            //类型
            Map<String, String> dictItemMap = new HashMap<>(16);
            Future<ResponseData<Map<String, String>>> in = ThreadUtil.execAsync(() -> {
                return dictService.getDictItemMapByParentCode(SystemDictCodeConstant.SPARE_PART_IN_TYPE);
            });
            Future<ResponseData<Map<String, String>>> out = ThreadUtil.execAsync(() -> {
                return dictService.getDictItemMapByParentCode(SystemDictCodeConstant.SPARE_PART_OUT_TYPE);
            });
            try {
                ResponseData<Map<String, String>> inResult = in.get();
                if(inResult.isSuccess() && CollUtil.isNotEmpty(inResult.getData())) {
                    dictItemMap.putAll(inResult.getData());
                }
                ResponseData<Map<String, String>> outResult = out.get();
                if(outResult.isSuccess() && CollUtil.isNotEmpty(outResult.getData())) {
                    dictItemMap.putAll(outResult.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.forEach(e -> {
                if(dictItemMap.containsKey(e.getType())) {
                    e.setTypeDesc(dictItemMap.get(e.getType()));
                }
            });
        }
        queryPage.setRecords(list);
        return queryPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addStorageRecord(SparePartStorageRecord sparePartStorageRecord) throws CommonException {
        //查询当前库存
        List<SparePartStorageRecordDetail> detailList = sparePartStorageRecord.getDetailList();
        if(CollUtil.isEmpty(detailList)) {
            throw new CommonException("出库备件列表不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        for (SparePartStorageRecordDetail item : detailList) {
            if(StrUtil.isBlank(item.getWareHouseId()) || StrUtil.isBlank(item.getWareHouseName())) {
                throw new CommonException("仓库id或仓库名称不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            if(StrUtil.isBlank(item.getSparePartId())) {
                throw new CommonException("备件id不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        Set<String> sparePartIds = detailList.stream().map(SparePartStorageRecordDetail::getSparePartId).collect(Collectors.toSet());
        List<SparePartInventory> sparePartInventories = sparePartInventoryService.listBySparePartIds(sparePartIds);
        Map<String, SparePartInventory> saprePartInventorMap = sparePartInventories.stream().collect(Collectors.toMap(k->k.getSparePartId()+k.getWareHouseId(), v -> v));
        //检查库存
        if(!sparePartStorageRecord.getIsIn()) {
            for (SparePartStorageRecordDetail sparePartStorageRecordDetail : detailList) {
                SparePartInventory sparePartInventory = saprePartInventorMap.get(sparePartStorageRecordDetail.getSparePartId()+sparePartStorageRecordDetail.getWareHouseId());
                if(sparePartStorageRecordDetail.getCount() == null) {
                    throw new CommonException("库存变动数量不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
                } else if(sparePartStorageRecordDetail.getCount() <= 0) {
                    throw new CommonException("库存变动数量必须大于0", CommonExceptionDefinition.BASIC_EXCEPTION);
                } else if(sparePartInventory == null || sparePartStorageRecordDetail.getCount() > sparePartInventory.getInventory()) {
                    throw new CommonException("库存不足", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
            }
        }

        //保存出入库单
        Date now = new Date();
        sparePartStorageRecord.setCreateUser(UserUtils.getUserId());
        sparePartStorageRecord.setCreateTime(now);
        if(sparePartStorageRecord.getRecordTime() == null) {
            sparePartStorageRecord.setRecordTime(now);
        }
        if(StrUtil.isEmpty(sparePartStorageRecord.getAgent())) {
            sparePartStorageRecord.setAgent(UserUtils.getUserName());
        }
        ResponseData<String> stringResponseData = codeRuleService.generatorNextNumber("BJGDBH");
        if (!ResultResponseEnum.SUCCESS.getCode().equals(stringResponseData.getCode())) {
            throw new CommonException(stringResponseData.getMsg(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        sparePartStorageRecord.setOrderNo(stringResponseData.getData());
        boolean saved = save(sparePartStorageRecord);

        String recordId = sparePartStorageRecord.getId();
        if(saved) {

            //保存出入库详情
            List<SparePartInventory> updateList = new ArrayList<>();
            detailList.forEach(e -> {
                SparePartInventory sparePartInventory = saprePartInventorMap.getOrDefault(e.getSparePartId()+e.getWareHouseId(), new SparePartInventory());
                sparePartInventory.setSparePartId(e.getSparePartId());
                sparePartInventory.setWareHouseId(e.getWareHouseId());
                sparePartInventory.setWareHouseName(e.getWareHouseName());
                sparePartInventory.setInventory(sparePartInventory.getInventory() == null ? 0 : sparePartInventory.getInventory());
                e.setRecordId(recordId);
                e.setId(null);
                if(sparePartStorageRecord.getIsIn()) {
                    e.setInCount(e.getCount());
                    e.setOutCount(0);
                } else {
                    e.setInCount(0);
                    e.setOutCount(e.getCount());
                }
                int currentInventory = sparePartInventory.getInventory() - e.getOutCount() + e.getInCount();
                sparePartInventory.setInventory(currentInventory);
                e.setInventory(currentInventory);
                updateList.add(sparePartInventory);
            });
            sparePartStorageRecordDetailService.saveBatch(detailList);

            //更新库存
            sparePartInventoryService.saveOrUpdateBatch(updateList);
        }
        return saved;
    }

    @Override
    public Boolean addStorageRecord(SparePartStorageRequestDTO sparePartStorageRequestDTO) throws CommonException {
        SparePartStorageRecord sparePartStorageRecord = new SparePartStorageRecord();
        BeanUtils.copyProperties(sparePartStorageRequestDTO, sparePartStorageRecord);
        List<SparePartStorageDetailRequestDTO> details = sparePartStorageRequestDTO.getDetails();
        List<SparePartStorageRecordDetail> sparePartStorageRecordDetails = new ArrayList<>();
        if(CollUtil.isNotEmpty(details)) {
            for (SparePartStorageDetailRequestDTO detail : details) {
                SparePartStorageRecordDetail item = new SparePartStorageRecordDetail();
                BeanUtils.copyProperties(detail, item);
                sparePartStorageRecordDetails.add(item);
            }
        }
        sparePartStorageRecord.setDetailList(sparePartStorageRecordDetails);
        return addStorageRecord(sparePartStorageRecord);
    }

    @Override
    public Boolean outByMaintenance(SparePartStorageRequestDTO sparePartStorageRequestDTO) throws CommonException {
        sparePartStorageRequestDTO.setType("WXLL");
        sparePartStorageRequestDTO.setIsIn(false);
        return addStorageRecord(sparePartStorageRequestDTO);
    }

    @Override
    public SparePartStorageRecordDTO detail(String id) {
        return baseMapper.detail(id);
    }

    @Override
    public List<SparePartStorageRecordDTO> detailListByReferenceNo(String referenceOrderNo) {
        List<SparePartStorageRecordDTO> list = baseMapper.detailListByReferenceId(referenceOrderNo);
        //备件类型信息
        ResponseData<Map<String, String>> dictItemMap = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.SPARE_PART_TYPE);
        if(dictItemMap.isSuccess()) {
            Map<String, String> map = dictItemMap.getData();
            list.forEach(e -> {
                if(map.containsKey(e.getSparePartTypeCode())) {
                    e.setSparePartTypeDesc(map.get(e.getSparePartTypeCode()));
                }
            });
        }
        return list;
    }

    @Override
    public SparepartStorageCountStatisticsDTO storageCountStatistics(String site) {
        return baseMapper.storageCountStatistics(site);
    }

    @Override
    public Page<SparePartChangeRecordDTO> sparePartChangeRecord(Map<String, Object> params) {
        Page<SparePartChangeRecordDTO> page = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        List<SparePartChangeRecordDTO> list = baseMapper.sparePartChangeRecord(page, params);
        if(CollUtil.isNotEmpty(list)) {
            //备件类型信息
            ResponseData<Map<String, String>> dictItemMap = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.SPARE_PART_TYPE);
            if (dictItemMap.isSuccess()) {
                Map<String, String> map = dictItemMap.getData();
                list.forEach(e -> {
                    if (map.containsKey(e.getType())) {
                        e.setTypeDesc(map.get(e.getType()));
                    }
                });
            }
            //供应商
            List<String> ids = list.stream().map(SparePartChangeRecordDTO::getSparePartId).collect(Collectors.toList());
            ResponseData<List<Map<String, Object>>> result = supplierFeignService.getByIds(ids);
            if(result.isSuccess()) {
                List<Map<String, Object>> data = result.getData();
                Map<String, String> map = data.stream().collect(Collectors.toMap(e -> (String) e.get("id"), e -> (String) e.get("supplierName")));
                list.forEach(e -> {
                    if(map.containsKey(e.getSupplier())) {
                        e.setSupplierName(map.get(e.getSupplier()));
                    }
                });
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<SparePartStorageRecordDetail> listByRecordId(Map<String, Object> params) {
        return sparePartStorageRecordDetailService.listByRecordId(params);
    }
}
