package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.constant.CheckResultEnum;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.DefCodeInfoDto;
import com.itl.mes.core.api.dto.QualitativeInspectionDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.TempDateSaveRequestDto;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.entity.Station;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.client.service.MeProductInspectionItemsOrderService;
import com.itl.mes.core.provider.mapper.QualitativeInspectionMapper;
import com.itl.mes.me.api.dto.MeProductInspectionItemsOrderDto;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定量检验service
 * @author dengou
 * @date 2021/11/16
 */
@Service
public class QuantifyInspectionServiceImpl {

    @Autowired
    private QualitativeInspectionMapper qualitativeInspectionMapper;
    @Autowired
    private StationService stationService;
    @Autowired
    private NcCodeService ncCodeService;
    @Autowired
    private MeProductInspectionItemsOrderService meProductInspectionItemsOrderService;
    @Autowired
    private SnService snService;
    @Autowired
    private ITemporaryDataService temporaryDataService;
    @Autowired
    private MeProductInspectionItemsOrderNcCodeService meProductInspectionItemsOrderNcCodeService;



    /**
     * 获取定量检验列表
     * 查询当前工位编码(改为前端传入)
     * 工位编码 去【工位维护】表 获取工序编码
     *
     * 根据SN 去 【工单标签】的条形码【条码标】表中查询 工单编码
     *
     * 根据 工单编码 去【工单维护】的【检验项目】标签并过滤  拿到相关信息
     * 	过滤条件：
     * 		工序+检验标识
     * 		（工序编码）+ （定性）
     */
    public List<MeProductInspectionItemsOrderDto> getProductInspectionItems(QualitativeInspectionDTO dto) {
        Assert.valid(StrUtil.isBlank(dto.getSn()), "sn不能为空");
        String site = UserUtils.getSite();
        if(StrUtil.isBlank(dto.getSite())) {
            dto.setSite(site);
        }
        dto.setStationBo(new StationHandleBO(dto.getSite(), UserUtils.getStation()).getBo());

        Station station = stationService.lambdaQuery()
                .select(Station::getOperationBo)
                .eq(Station::getSite, dto.getSite())
                .eq(Station::getBo, dto.getStationBo()).eq(Station::getState, 1).one();


        Assert.valid(station == null, "工位未维护");
        //工序编号
        String operationBo = station.getOperationBo();
        if(StringUtils.isEmpty(operationBo)){
            return Collections.emptyList();
        }

        return qualitativeInspectionMapper.findProductInspectionItemsByOperationBo(operationBo, new SnHandleBO(site, dto.getSn()).getBo(), site, "1");
    }


    /**
     * 定量检验结果暂存
     * @param sn 条码
     * @param list 检验结果
     * @return 是否成功
     * */
    public Boolean saveQuantifyInspection(List<TempDateSaveRequestDto> list, String sn) {
        Assert.valid(CollUtil.isEmpty(list), "暂存数据不能为空");
        for (TempDateSaveRequestDto item : list) {
            Assert.valid(StrUtil.isBlank(item.getTest()), "测试值不能为空");
        }
        String userName = UserUtils.getUserName();
        String station = UserUtils.getStation();
        String site = UserUtils.getSite();
        String workShop = UserUtils.getWorkShop();
        String stationBo = new StationHandleBO(site, station).getBo();
        //校验sn
        Boolean snExists = snService.checkExistsBySn(sn, site);
        Assert.valid(!snExists, "条码不存在");

        //查询检验项目
        List<Integer> ids = list.stream().map(TempDateSaveRequestDto::getId).collect(Collectors.toList());
        ResponseData<Collection<MeProductInspectionItemsOrderEntity>> result = meProductInspectionItemsOrderService.listByIds(ids);
        Collection<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderList = new ArrayList<>();
        if(result.isSuccess()) {
            CollUtil.addAll(meProductInspectionItemsOrderList, result.getData());
        }
        Map<Integer, MeProductInspectionItemsOrderEntity> idOrderMap = meProductInspectionItemsOrderList.stream().collect(Collectors.toMap(MeProductInspectionItemsOrderEntity::getId, e -> e));

        //查询不合格代码信息（缺陷代码）
        List<String> ncCodeBos = list.stream().filter(e -> CollUtil.isNotEmpty(e.getNcCodeBos())).flatMap(e -> e.getNcCodeBos().stream()).collect(Collectors.toList());
        List<NcCode> ncCodeList = ncCodeService.getNcCodeByIds(ncCodeBos, site);
        Map<String, NcCode> ncCodeIdMap = ncCodeList.stream().collect(Collectors.toMap(NcCode::getBo, e -> e));

        //temp content json object
        List<QualitativeInspectionSaveDTO> qualitativeInspectionSaveDTOS = new ArrayList<>();

        for (TempDateSaveRequestDto tempDateSaveRequestDto : list) {
            QualitativeInspectionSaveDTO item = new QualitativeInspectionSaveDTO();

            //设置条码信息
            item.setSn(sn);
            //设置测试值
            item.setTest(tempDateSaveRequestDto.getTest());

            //设置检验项目信息
            MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = idOrderMap.get(tempDateSaveRequestDto.getId());
            if(meProductInspectionItemsOrderEntity != null) {
                item.setProjectName(meProductInspectionItemsOrderEntity.getCheckProject());
                item.setProjectId(meProductInspectionItemsOrderEntity.getId()+"");
                item.setLowerLimit(meProductInspectionItemsOrderEntity.getLowerLimit());
                item.setUpperLimit(meProductInspectionItemsOrderEntity.getUpperLimit());

                //根据test值计算结果
                BigDecimal test = new BigDecimal(item.getTest());
                String lowerLimitStr = meProductInspectionItemsOrderEntity.getLowerLimit();
                String upperLimitStr = meProductInspectionItemsOrderEntity.getUpperLimit();

                Boolean lowerResult = false, upperResult = false;

                //下限值为空，lowerResult为true
                if(StrUtil.isBlank(lowerLimitStr)) {
                    lowerResult = true;
                } else {
                    //获取检验上限值和下限值
                    if(NumberUtil.isNumber(lowerLimitStr)) {
                        BigDecimal lowerLimit = new BigDecimal(meProductInspectionItemsOrderEntity.getLowerLimit());
                        //判断检验符号
                        String lowerLimitSymbol = meProductInspectionItemsOrderEntity.getLowerLimitSymbol();
                        if (StrUtil.equals(lowerLimitSymbol, "0")) {
                            lowerResult = test.compareTo(lowerLimit) > 0;
                        } else {
                            lowerResult = test.compareTo(lowerLimit) >= 0;
                        }
                    }
                }

                //上限值为空，upperResult为true
                if(StrUtil.isBlank(upperLimitStr)) {
                    upperResult = true;
                } else {
                    if(NumberUtil.isNumber(upperLimitStr)) {
                        BigDecimal upperLimit = new BigDecimal(meProductInspectionItemsOrderEntity.getUpperLimit());
                        String upperLimitSymbol = meProductInspectionItemsOrderEntity.getUpperLimitSymbol();

                        if (StrUtil.equals(upperLimitSymbol, "0")) {
                            upperResult = test.compareTo(upperLimit) < 0;
                        } else {
                            upperResult = test.compareTo(upperLimit) <= 0;
                        }
                    }
                }

                item.setResult(lowerResult && upperResult ? CheckResultEnum.OK.getCode() : CheckResultEnum.NG.getCode());
            }


            //设置检验人信息
            item.setSurveyor(userName);
            item.setStationBo(stationBo);
            item.setWorkShop(workShop);
            item.setStation(station);

            //设置不合格代码信息（缺陷记录）
            List<String> itemNcCodeBos = tempDateSaveRequestDto.getNcCodeBos();
            List<DefCodeInfoDto> defCodeInfoDtos = new ArrayList<>();
            for (String itemNcCodeBo : itemNcCodeBos) {
                NcCode ncCode = ncCodeIdMap.get(itemNcCodeBo);
                DefCodeInfoDto defCodeInfoDto = new DefCodeInfoDto();
                defCodeInfoDto.setDefectCode(ncCode.getNcCode());
                defCodeInfoDto.setDefectRecord(ncCode.getNcName());
                defCodeInfoDto.setDefectDescription(ncCode.getNcDesc());
                if(meProductInspectionItemsOrderEntity != null) {
                    defCodeInfoDto.setDescriptionOfInspectionItems(meProductInspectionItemsOrderEntity.getCheckProject());
                }

                defCodeInfoDtos.add(defCodeInfoDto);
            }
            //如果检验结果为ng， 除了保存选中的不合格代码外，还需要保存产品/产品组 检验项目默认的不合格代码
            if(StrUtil.equals(CheckResultEnum.NG.getCode(), item.getResult())) {
                //获取产品检验项目不合格代码副本记录
                String orderBo = meProductInspectionItemsOrderEntity.getOrderBo();
                Integer inspectionItemId = meProductInspectionItemsOrderEntity.getId();
                List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = meProductInspectionItemsOrderNcCodeService.listByOrderAndInspection(orderBo, inspectionItemId);
                if (CollUtil.isNotEmpty(meProductInspectionItemsOrderNcCodeList)) {
                    List<String> codeList = defCodeInfoDtos.stream().map(DefCodeInfoDto::getDefectCode).collect(Collectors.toList());
                    meProductInspectionItemsOrderNcCodeList.forEach(e -> {
                        //已存在， 跳过
                        if (codeList.contains(e.getNcCode())) {
                            return;
                        }
                        DefCodeInfoDto defCodeInfoDto = new DefCodeInfoDto();
                        defCodeInfoDto.setDefectCode(e.getNcCode());
                        defCodeInfoDto.setDefectRecord(e.getNcName());
                        defCodeInfoDto.setDefectDescription(e.getNcDesc());
                        if (meProductInspectionItemsOrderEntity != null) {
                            defCodeInfoDto.setDescriptionOfInspectionItems(meProductInspectionItemsOrderEntity.getCheckProject());
                        }

                        defCodeInfoDtos.add(defCodeInfoDto);
                    });
                }
            }

            item.setDefCodeList(defCodeInfoDtos);

            qualitativeInspectionSaveDTOS.add(item);
        }

        if(CollUtil.isNotEmpty(qualitativeInspectionSaveDTOS)) {
            TemporaryData temporaryData = new TemporaryData();
            temporaryData.setSn(sn);
            temporaryData.setContent(JSONUtil.toJsonStr(qualitativeInspectionSaveDTOS));
            temporaryData.setStation(station);
            temporaryData.setType(TemporaryDataTypeEnum.QUANTIFY.getCode());
            return temporaryDataService.addOrUpdate(temporaryData);
        }
        return true;
    }


    /**
     * 根据sn和station查询暂存数据
     * @param sn 条码
     * @param station 工位
     * @return 暂存数据
     * */
    public List<QualitativeInspectionSaveDTO> getBySnAndStation(String sn, String station) {
        TemporaryData temporaryData = temporaryDataService.getBySnAndStation(sn, station, TemporaryDataTypeEnum.QUANTIFY.getCode());
        if(ObjectUtil.isNull(temporaryData)) {
            return null;
        }
        String content = temporaryData.getContent();
        if(StrUtil.isBlank(content)) {
            return null;
        }
        return JSONUtil.parseArray(content).toList(QualitativeInspectionSaveDTO.class);
    }


}
