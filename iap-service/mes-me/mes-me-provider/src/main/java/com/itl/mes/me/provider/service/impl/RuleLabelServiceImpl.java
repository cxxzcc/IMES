package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.client.service.LabelRuleQueryService;
import com.itl.mes.me.api.dto.ruleLabel.ItemColumns;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelQueryDto;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelSaveDto;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelShowDto;
import com.itl.mes.me.api.entity.RuleLabelDetail;
import com.itl.mes.me.api.entity.RuleLabel;
import com.itl.mes.me.api.enums.FLAG;
import com.itl.mes.me.api.service.RuleLabelDetailService;
import com.itl.mes.me.api.util.GeneratorId;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itl.mes.me.provider.mapper.RuleLabelMapper;
import com.itl.mes.me.api.service.RuleLabelService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class RuleLabelServiceImpl extends ServiceImpl<RuleLabelMapper, RuleLabel> implements RuleLabelService {
    @Resource
    private RuleLabelMapper ruleLabelMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private RuleLabelDetailService ruleLabelDetailService;

    @Autowired
    private LabelRuleQueryService labelRuleQueryService;

    @Autowired
    private CodeRuleService codeRuleService;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAndUpdate(RuleLabelSaveDto saveDto) throws CommonException {
        verify(saveDto.getBo(), saveDto.getRuleLabel(), saveDto.getElementType());

        calibrateItemAndLabel(saveDto.getDetails());

        String userName = userUtil.getUser().getUserName();
        String site = UserUtils.getSite();

        List<String> list = Stream.of(FLAG.ITEM.getCustomDataType(),
                FLAG.SHOP_ORDER.getCustomDataType(),
                FLAG.CARRIER.getCustomDataType(),
                FLAG.DEVICE.getCustomDataType(),
                FLAG.PACK.getCustomDataType()).collect(Collectors.toList());

        // 获取元素自定义字段名称
        List<String> customs = ruleLabelMapper.getCustoms(site, list);

        if (StrUtil.isBlank(saveDto.getBo())) {
            insertEntity(saveDto, site, userName, customs);
        } else {
            updateEntity(saveDto, site, userName, customs);
        }
    }

    private void verify(String bo, String ruleLabel, String elementType) throws CommonException {
        LambdaQueryWrapper<RuleLabel> query = new QueryWrapper<RuleLabel>().lambda()
                .eq(RuleLabel::getRuleLabel, ruleLabel)
                .eq(RuleLabel::getElementType, elementType);
        Optional.ofNullable(bo).ifPresent(x -> query.ne(RuleLabel::getBo, bo));

        if (CollUtil.isNotEmpty(list(query))) {
            throw new CommonException("该类型下编号已存在!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public IPage<RuleLabelShowDto> queryPage(RuleLabelQueryDto queryDto) throws CommonException {
        queryPreProcess(queryDto);

        try {
            return ruleLabelMapper.queryList(queryDto.getPage(), queryDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    /**
     * 查询条件预处理
     *
     * @param queryDto
     */
    private void queryPreProcess(RuleLabelQueryDto queryDto) {
        if (queryDto.getPage() == null) {
            queryDto.setPage(new Page(0, 10));
        }
        queryDto.getPage().setDesc("irl.CREATE_DATE");
        queryDto.setSite(UserUtils.getSite());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(String[] bos) throws CommonException {
        try {
            LambdaQueryWrapper<RuleLabelDetail> query = new QueryWrapper<RuleLabelDetail>().lambda().in(RuleLabelDetail::getIrlBo, bos);
            ruleLabelDetailService.remove(query);

            ruleLabelMapper.deleteBatchIds(Arrays.asList(bos));
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public List<ItemColumns> getColumns(FLAG flag) {
        String site = UserUtils.getSite();
        return ruleLabelMapper.getColumns(site, flag.getTableName(), flag.getCustomDataType());
    }

    @Override
    public List<ItemColumns> getColumns(FLAG flag, FLAG flag2) {
        String site = UserUtils.getSite();
        return ruleLabelMapper.getColumnsP(site, flag.getCustomDataType());
    }

    @Override
    public List<LabelEntityParams> getLabelEntityParams(String id) {

        return ruleLabelMapper.getLabelEntityParams(id);
    }

    @Override
    public List<String> generatorCode(String bo, String elementBo, Integer number,Map<String,Object> paramsMap) throws CommonException {
        // 查询itemRuleLabel和itemRuleLabelDetail
        RuleLabel ruleLabel = getById(bo);
        List<RuleLabelDetail> list = ruleLabelDetailService.list(
                new QueryWrapper<RuleLabelDetail>()
                        .lambda()
                        .eq(RuleLabelDetail::getIrlBo, bo));

        if (CollectionUtil.isNotEmpty(list)) {
            // 字段 <--> 值Map
            Map<String, Object> params = this.getItemParams(list, UserUtils.getSite(), elementBo, ruleLabel.getElementType( ));

            if (ObjectUtil.isNotEmpty(params)) {
                // 变量 <--> 值Map
                Map<String, Object> parameters = new HashMap<>( );
                list.forEach(data -> {
                    if (paramsMap.containsKey(data.getRuleVar( ))) {
                        for (String key : paramsMap.keySet( )) {
                            if (key.equals(data.getRuleVar( ))) {
                                parameters.put(data.getRuleVar( ), paramsMap.get(key));
                                break;
                            }
                        }
                    } else {
                        if (StrUtil.isNotBlank(data.getRuleVar( ))) {
                            parameters.put(data.getRuleVar( ), params.get(data.getRuleVal( )));
                        }
                    }
                });
                ResponseData<List<String>> listResponseData = codeRuleService.generatorNextNumberList(new CodeGenerateDto(ruleLabel.getCodeRuleBo(), number, parameters));
                if(!ResultResponseEnum.SUCCESS.getCode().equals(listResponseData.getCode())){
                    throw new CommonException(listResponseData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION );
                }
                List<String> codes = listResponseData.getData();
                return codes;
            } else {
                ResponseData<List<String>> listResponseData = codeRuleService.generatorNextNumberList(new CodeGenerateDto(ruleLabel.getCodeRuleBo( ), number, paramsMap));
                if(!ResultResponseEnum.SUCCESS.getCode().equals(listResponseData.getCode())){
                    throw new CommonException(listResponseData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION );
                }
                List<String> codes = listResponseData.getData();
                return codes;
            }
        }
        ResponseData<List<String>> listResponseData = codeRuleService.generatorNextNumberList(new CodeGenerateDto(ruleLabel.getCodeRuleBo(), number, null));
        if(!ResultResponseEnum.SUCCESS.getCode().equals(listResponseData.getCode())){
            throw new CommonException(listResponseData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        return listResponseData.getData();

    }

    @Override
    public RuleLabelShowDto getByBo(String bo) {
        return ruleLabelMapper.getByBo(bo);
    }

    private void insertEntity(RuleLabelSaveDto saveDto, String site, String userName, List<String> customs) {

        // 生成ID
        String bo = new GeneratorId().getSnowflake().nextIdStr();

        RuleLabel toSave = new RuleLabel();
        BeanUtil.copyProperties(saveDto, toSave);

        toSave.setBo(bo).setSite(site)
                .setCodeRuleBo(new CodeRuleHandleBO(site, saveDto.getCodeRuleType()).getBo())
                .setObjectSetBasicAttribute(userName, new Date());

        ruleLabelMapper.insert(toSave);

        if (CollectionUtil.isNotEmpty(saveDto.getDetails())) {
            saveDto.getDetails().forEach(data -> data.setIrlBo(bo)
                    .setIsCustom(customs.contains(data.getRuleVal()) ? "Y" : "N"));
            ruleLabelDetailService.saveBatch(saveDto.getDetails());
        }
    }

    private void updateEntity(RuleLabelSaveDto saveDto, String site, String userName, List<String> customs) {
        RuleLabel toUpdate = new RuleLabel();
        BeanUtil.copyProperties(saveDto, toUpdate);

        toUpdate.setSite(site)
                .setCodeRuleBo(new CodeRuleHandleBO(site, saveDto.getCodeRuleType()).getBo())
                .setModifyUser(userName).setModifyDate(new Date());

        ruleLabelMapper.updateById(toUpdate);

        // 移除原有的明细
        ruleLabelDetailService.remove(
                new QueryWrapper<RuleLabelDetail>().lambda().eq(RuleLabelDetail::getIrlBo, saveDto.getBo())
        );
        if (CollectionUtil.isNotEmpty(saveDto.getDetails())) {
            saveDto.getDetails().forEach(data -> data.setIrlBo(saveDto.getBo())
                    .setIsCustom(customs.contains(data.getRuleVal()) ? "Y" : "N"));
            ruleLabelDetailService.saveBatch(saveDto.getDetails());
        }
    }

    /**
     * 获取该模板涉及元素的字段及其取值
     *
     * @param list
     * @param site
     * @param elementBo
     * @param elementType
     * @return
     */
    private Map<String, Object> getItemParams(List<RuleLabelDetail> list, String site, String elementBo, String elementType) {
        Map<String, Object> params = new HashMap<>();

        // 拼装涉及的字段
        ItemForParamQueryDto yQuery = new ItemForParamQueryDto()
                .setSite(site)
                .setIsCustom("Y")
                .setElementType(elementType)
                .setElementBo(elementBo);
        ItemForParamQueryDto nQuery = new ItemForParamQueryDto()
                .setSite(site)
                .setIsCustom("N")
                .setElementType(elementType)
                .setElementBo(elementBo);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(data -> {
                if (StrUtil.isNotBlank(data.getRuleVal())) {
                    if ("Y".equals(data.getIsCustom())) {
                        yQuery.getColumns().add(data.getRuleVal());
                    } else if ("N".equals(data.getIsCustom())) {
                        nQuery.getColumns().add(data.getRuleVal());
                    }
                }
            });
        }

        // 查询涉及字段的取值
        if (yQuery.getColumns().size() > 0) {
            Map<String, Object> yParams = labelRuleQueryService.getParams(yQuery);
            params.putAll(yParams);
        }
        if (nQuery.getColumns().size() > 0) {
            Map<String, Object> nParams = labelRuleQueryService.getParams(nQuery);
            params.putAll(nParams);
        }

        return params;
    }

    /**
     * 验证编码变量_模板变量
     *
     * @param list
     * @throws CommonException
     */
    private void calibrateItemAndLabel(List<RuleLabelDetail> list) throws CommonException {
        for (RuleLabelDetail x : list) {
            if (StringUtils.isNotBlank(x.getRuleVar())) {
                if (StringUtils.isNotBlank(x.getTemplateArg())) {
                    throw new CommonException("编码变量和模板变量不能都存在!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
            } else {
                if (x.getTemplateArg().isEmpty()) {
                    throw new CommonException("编码变量和模板变量不能都为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
            }
        }
    }
}
