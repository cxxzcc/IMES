package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.client.service.LabelService;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mes.core.client.service.SnService;
import com.itl.mes.me.api.dto.LabelPrintBarCodeDto;
import com.itl.mes.me.api.dto.LabelPrintQueryDto;
import com.itl.mes.me.api.dto.LabelPrintSaveDto;
import com.itl.mes.me.api.entity.LabelPrint;
import com.itl.mes.me.api.entity.LabelPrintDetail;
import com.itl.mes.me.api.entity.LabelPrintLog;
import com.itl.mes.me.api.entity.RuleLabel;
import com.itl.mes.me.api.service.LabelPrintDetailService;
import com.itl.mes.me.api.service.LabelPrintLogService;
import com.itl.mes.me.api.service.LabelPrintService;
import com.itl.mes.me.api.service.RuleLabelService;
import com.itl.mes.me.api.vo.LabelPrintVo;
import com.itl.mes.me.provider.constant.ElementConstant;
import com.itl.mes.me.provider.enums.ElementEnum;
import com.itl.mes.me.provider.enums.IsComplementEnum;
import com.itl.mes.me.provider.enums.LabelTemplateTypeEnum;
import com.itl.mes.me.provider.mapper.LabelPrintDetailMapper;
import com.itl.mes.me.provider.mapper.LabelPrintMapper;
import com.itl.mes.me.provider.mapper.RuleLabelMapper;
import com.itl.mes.me.provider.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 标签打印范围 服务实现类
 * </p>
 *
 * @author zhongfei
 * @since 2021-01-20
 */
@Service
@Slf4j
public class LabelPrintServiceImpl extends ServiceImpl<LabelPrintMapper, LabelPrint> implements LabelPrintService {

    @Autowired
    LabelPrintMapper labelPrintMapper;

    @Autowired
    LabelPrintDetailService labelPrintDetailService;

    @Autowired
    LabelPrintDetailMapper labelPrintRangeDetailMapper;

    @Autowired
    RuleLabelService ruleLabelService;

    @Autowired
    RuleLabelMapper ruleLabelMapper;

    @Autowired
    LabelService labelService;

    @Autowired
    LabelPrintLogService labelPrintLogService;

    @Autowired
    private ShopOrderService shopOrderService;


    @Autowired
    CommonUtils commonUtils;

    @Autowired
    private SnService snService;

    /*@Autowired
    private MeProductStatusService meProductStatusService;*/


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLabelPrint(LabelPrintSaveDto labelPrintSaveDto) throws CommonException {

        //校验元素类型是否合法
        Boolean elementTypeCheck = false;
        for(ElementEnum elementEnum:ElementEnum.values()){
            if(elementEnum.getType().equals(labelPrintSaveDto.getElementType())){
                elementTypeCheck = true;
            }
        }
        if(!elementTypeCheck){
            throw new CommonException("不支持的元素类型",CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        LabelPrint labelPrint = new LabelPrint();
        BeanUtil.copyProperties(labelPrintSaveDto,labelPrint);
        String labelPrintId = UUID.randomUUID().toString().replace("-","");
        labelPrint.setBo(labelPrintId);

        RuleLabel itemRuleLabel = ruleLabelMapper.selectById(labelPrintSaveDto.getRuleLabelBo());
        try {
            if(ObjectUtil.isEmpty(itemRuleLabel)){
                throw new RuntimeException("未找到对应的物料标签模板，请先维护物料标签模板");
            }
        }catch (Exception e){
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //处理工单标签数量变更
        if(ElementEnum.SHOP_ORDER.getType().equals(labelPrintSaveDto.getElementType())){
            String shopOrderNum = labelPrintSaveDto.getElementBo().split(",")[1];
            ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderNum);
            if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
                throw new RuntimeException(shopOrderData.getMsg());
            }
            ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
            if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(labelPrintSaveDto.getBarCodeAmount())))<0){
                throw new RuntimeException("打印数量超出工单可打印数量");
            }
        }

        //定义编码规则参数Map,会自动去替换编码规则的值
        Map<String,Object> ruleLabelParams = JSONObject.parseObject(labelPrintSaveDto.getRuleLabelParams());
        List<String>  codeList= ruleLabelService.generatorCode(itemRuleLabel.getBo(),labelPrintSaveDto.getElementBo(),labelPrintSaveDto.getBarCodeAmount(),ruleLabelParams);
        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();

        List<LabelPrintDetail> labelPrintDetails = new ArrayList<>();
        //定义标签参数Map，用于后面取值去生成标签
        Map<String,Object> labelParams = JSONObject.parseObject(labelPrintSaveDto.getLabelParams());

        for(String code:codeList){
            if(code.equals(codeList.get(0))){
                labelPrint.setStartCode(code);
            }else if(code.equals(codeList.get(codeList.size()-1))){
                labelPrint.setEndCode(code);
            }
            LabelPrintDetail labelPrintDetail = new LabelPrintDetail();
            labelPrintDetail.setLabelPrintBo(labelPrintId);
            labelPrintDetail.setDetailCode(code);
            //遍历标签参数，当有数据未赋值时，将编码规则生成的code赋值上
            Map<String,Object> paramsMap = new HashMap<>();
            for(String key:labelParams.keySet()){
                if(ObjectUtil.isEmpty(labelParams.get(key))){
                    paramsMap.put(key,code);
                }else {
                    paramsMap.put(key,labelParams.get(key));
                }
            }
            //将标签数据转成json字符串存到数据库，以方便后面生成pdf文件
            labelPrintDetail.setLabelParams(JSON.toJSONString(paramsMap));
            labelPrintDetail.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty());
            labelPrintDetails.add(labelPrintDetail);
        }

        labelPrintDetailService.saveBatch(labelPrintDetails);
        labelPrint.setPrintDate(DateUtil.parseDate(DateUtil.today()));
        labelPrint.setCreateUser(userName);
        labelPrint.setCreateDate(date);
        labelPrint.setSite(UserUtils.getSite());

        labelPrintMapper.insert(labelPrint);

        //处理工单标签数量变更
        if(ElementEnum.SHOP_ORDER.getType().equals(labelPrintSaveDto.getElementType())){
            shopOrderService.updateShopOrderLabelQtyByBO(labelPrintSaveDto.getElementBo(),new BigDecimal(labelPrintSaveDto.getBarCodeAmount()));
        }

    }

    @Override
    public IPage<LabelPrintVo> findList(LabelPrintQueryDto labelPrintQueryDto) throws CommonException {
        if(ObjectUtil.isEmpty(labelPrintQueryDto.getPage())){
            labelPrintQueryDto.setPage(new Page(0,10));
        }
        labelPrintQueryDto.setSite(UserUtils.getSite());
        IPage<LabelPrintVo> labelPrintVoIPage;
        switch (labelPrintQueryDto.getElementType()){
            case ElementConstant.ITEM:
                labelPrintQueryDto.setTableName(ElementEnum.ITEM.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.ITEM.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(),labelPrintQueryDto);
                break;
            case ElementConstant.DEVICE:
                labelPrintQueryDto.setTableName(ElementEnum.DEVICE.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.DEVICE.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(),labelPrintQueryDto);
                break;
            case ElementConstant.CARRIER:
                labelPrintQueryDto.setTableName(ElementEnum.CARRIER.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.CARRIER.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(),labelPrintQueryDto);
                break;
            case ElementConstant.SHOP_ORDER:
                labelPrintQueryDto.setTableName(ElementEnum.SHOP_ORDER.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.SHOP_ORDER.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(),labelPrintQueryDto);
                break;
            case ElementConstant.PACKING:
                labelPrintQueryDto.setTableName(ElementEnum.PACKING.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.PACKING.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findPackingLabelPrint(labelPrintQueryDto.getPage(),labelPrintQueryDto);
                break;
            default:throw new CommonException("不支持的元素类型",CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        return labelPrintVoIPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> barCodePrint(LabelPrintBarCodeDto barCodeDto) throws CommonException {

        barCodeDto.setBo(barCodeDto.getBo().replace("=",""));
        LabelPrint labelPrint = labelPrintMapper.selectById(barCodeDto.getBo());

        //当标签打印是不补码并且有打印过的时候，需要提示不可补码
        if(labelPrint.getIsComplement().equals(IsComplementEnum.N.getValue())&& ObjectUtil.isNotEmpty(labelPrint.getPrintDate())){

            throw new CommonException("该标签不可补码", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        RuleLabel ruleLabel = ruleLabelMapper.selectById(labelPrint.getRuleLabelBo());

        if(ObjectUtil.isEmpty(ruleLabel)){

            throw new CommonException("未找到对应的规则模板，未找到对应的规则模板", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();
        labelPrint.setPrintDate(date);
        labelPrintMapper.updateById(labelPrint);

        QueryWrapper<LabelPrintDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_print_bo",barCodeDto.getBo());
        if(StrUtil.isNotBlank(barCodeDto.getStartBarCode())&&StrUtil.isNotBlank(barCodeDto.getEndBarCode())){
            queryWrapper.between("detail_code",barCodeDto.getStartBarCode(),barCodeDto.getEndBarCode());
        }
        queryWrapper.orderByAsc("detail_code");
        List<LabelPrintDetail> labelPrintDetails = labelPrintRangeDetailMapper.selectList(queryWrapper);


        List<LabelPrintLog> labelPrintLogs = new ArrayList<>();

        List<Map<String,Object>> mapList  = new ArrayList<>();
        //返回的字符串集合，不同模板类型的返回值不一样
        List<String> respLists = new ArrayList<>();

        if(CollectionUtil.isNotEmpty(labelPrintDetails)){
            Map<String,Object> map = null;
            LabelPrintDetail labelPrintDetail = null;
            ListIterator<LabelPrintDetail> detailListIterator = labelPrintDetails.listIterator();
            while (detailListIterator.hasNext()){
                //处理并行数据
                if(CollectionUtil.isNotEmpty(barCodeDto.getLabelParamsList())){
                    for(int i=0;i<barCodeDto.getParallelAmount();i++){
                        if(i==0){
                            map = JSONObject.parseObject(labelPrintDetails.get(0).getLabelParams());
                        }
                        if(detailListIterator.hasNext()){
                            labelPrintDetail = detailListIterator.next();
                            labelPrintDetail.setLastPrintDate(date);
                            labelPrintDetail.setLastPrintUser(userName);
                            labelPrintDetail.setPrintCount(labelPrintDetail.getPrintCount()+1);
                            labelPrintRangeDetailMapper.updateById(labelPrintDetail);
                            //日志表赋值
                            LabelPrintLog labelPrintLog = new LabelPrintLog();
                            labelPrintLog.setLabelPrintBo(labelPrint.getBo());
                            labelPrintLog.setLabelPrintDetailBo(labelPrintDetail.getBo());
                            labelPrintLog.setPrintDate(date);
                            labelPrintLog.setPrintUser(userName);
                            labelPrintLogs.add(labelPrintLog);

                            map.put(barCodeDto.getLabelParamsList().get(i),labelPrintDetail.getDetailCode());
                        }

                    }
                }else {
                    labelPrintDetail = detailListIterator.next();
                    labelPrintDetail.setLastPrintDate(date);
                    labelPrintDetail.setLastPrintUser(userName);
                    labelPrintDetail.setPrintCount(labelPrintDetail.getPrintCount()+1);
                    labelPrintRangeDetailMapper.updateById(labelPrintDetail);
                    //日志表赋值
                    LabelPrintLog labelPrintLog = new LabelPrintLog();
                    labelPrintLog.setLabelPrintBo(labelPrint.getBo());
                    labelPrintLog.setLabelPrintDetailBo(labelPrintDetail.getBo());
                    labelPrintLog.setPrintDate(date);
                    labelPrintLog.setPrintUser(userName);
                    labelPrintLogs.add(labelPrintLog);
                    if(labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())){
                        map = JSONObject.parseObject(labelPrintDetails.get(0).getLabelParams());
                    }
                }

                if(labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())){
                    mapList.add(map);
                }else if(labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.LODOP.getValue())){
                    respLists.add(CollectionUtil.isNotEmpty(map)?JSONObject.toJSONString(map):labelPrintDetail.getLabelParams());
                }
            }

            labelPrintLogService.saveBatch(labelPrintLogs);
        }else {
            throw new CommonException("没有找到条码明细", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        if(labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())) {
            respLists = commonUtils.conversionUrl(labelService.batchCreatePdf(mapList, ruleLabel.getLabelBo()));
        }

        return respLists;

    }


}
