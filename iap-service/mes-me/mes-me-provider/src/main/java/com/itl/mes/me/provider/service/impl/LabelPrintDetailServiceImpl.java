package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.client.service.LabelService;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.client.service.ItemFeignService;
import com.itl.mes.me.api.dto.LabelPrintDetailQueryDto;
import com.itl.mes.me.api.entity.LabelPrint;
import com.itl.mes.me.api.entity.LabelPrintDetail;
import com.itl.mes.me.api.entity.LabelPrintLog;
import com.itl.mes.me.api.entity.RuleLabel;
import com.itl.mes.me.api.service.LabelPrintDetailService;
import com.itl.mes.me.api.vo.CheckBarCodeVo;
import com.itl.mes.me.api.vo.LabelPrintDetailVo;
import com.itl.mes.me.provider.constant.ElementConstant;
import com.itl.mes.me.provider.enums.ElementEnum;
import com.itl.mes.me.provider.enums.IsComplementEnum;
import com.itl.mes.me.provider.enums.LabelTemplateTypeEnum;
import com.itl.mes.me.provider.mapper.LabelPrintDetailMapper;
import com.itl.mes.me.provider.mapper.LabelPrintLogMapper;
import com.itl.mes.me.provider.mapper.LabelPrintMapper;
import com.itl.mes.me.provider.mapper.RuleLabelMapper;
import com.itl.mes.me.provider.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>
 * 标签打印范围明细 服务实现类
 * </p>
 *
 * @author liuchenghao
 * @since 2021-01-20
 */
@Service
public class LabelPrintDetailServiceImpl extends ServiceImpl<LabelPrintDetailMapper, LabelPrintDetail> implements LabelPrintDetailService {

    @Autowired
    private LabelPrintDetailMapper labelPrintDetailMapper;

    @Autowired
    private LabelPrintMapper labelPrintMapper;

    @Autowired
    private RuleLabelMapper ruleLabelMapper;

    @Autowired
    private LabelService labelService;

    @Autowired
    private LabelPrintLogMapper labelPrintLogMapper;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ItemFeignService itemFeignService;

    @Override
    public IPage<LabelPrintDetailVo> findList(LabelPrintDetailQueryDto labelPrintDetailQueryDto) throws CommonException {

        if(ObjectUtil.isEmpty(labelPrintDetailQueryDto.getPage())){
            labelPrintDetailQueryDto.setPage(new Page(0,10));
        }

        IPage<LabelPrintDetailVo> labelPrintDetailVoIPage;
        switch (labelPrintDetailQueryDto.getElementType()){
            case ElementConstant.ITEM:
                labelPrintDetailQueryDto.setTableName(ElementEnum.ITEM.getTableName());
                labelPrintDetailQueryDto.setQueryColumn(ElementEnum.ITEM.getQueryColumn());
                labelPrintDetailVoIPage = labelPrintDetailMapper.findList(labelPrintDetailQueryDto.getPage(),labelPrintDetailQueryDto);
                break;
            case ElementConstant.DEVICE:
                labelPrintDetailQueryDto.setTableName(ElementEnum.DEVICE.getTableName());
                labelPrintDetailQueryDto.setQueryColumn(ElementEnum.DEVICE.getQueryColumn());
                labelPrintDetailVoIPage = labelPrintDetailMapper.findList(labelPrintDetailQueryDto.getPage(),labelPrintDetailQueryDto);
                break;
            case ElementConstant.CARRIER:
                labelPrintDetailQueryDto.setTableName(ElementEnum.CARRIER.getTableName());
                labelPrintDetailQueryDto.setQueryColumn(ElementEnum.CARRIER.getQueryColumn());
                labelPrintDetailVoIPage = labelPrintDetailMapper.findList(labelPrintDetailQueryDto.getPage(),labelPrintDetailQueryDto);
                break;
            case ElementConstant.SHOP_ORDER:
                labelPrintDetailQueryDto.setTableName(ElementEnum.SHOP_ORDER.getTableName());
                labelPrintDetailQueryDto.setQueryColumn(ElementEnum.SHOP_ORDER.getQueryColumn());
                labelPrintDetailVoIPage = labelPrintDetailMapper.findList(labelPrintDetailQueryDto.getPage(),labelPrintDetailQueryDto);
                break;
            case ElementConstant.PACKING:
                labelPrintDetailQueryDto.setTableName(ElementEnum.PACKING.getTableName());
                labelPrintDetailQueryDto.setQueryColumn(ElementEnum.PACKING.getQueryColumn());
                labelPrintDetailVoIPage = labelPrintDetailMapper.findPackingLabelPrintDetail(labelPrintDetailQueryDto.getPage(),labelPrintDetailQueryDto);
                break;
            default:throw new CommonException("不支持的元素类型",CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return labelPrintDetailVoIPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> barCodeDetailPrint(String detailBo) throws CommonException {

        detailBo = detailBo.replace("=","");
        LabelPrintDetail labelPrintDetail = labelPrintDetailMapper.selectById(detailBo);
        LabelPrint labelPrint = labelPrintMapper.selectById(labelPrintDetail.getLabelPrintBo());

        if(labelPrint.getIsComplement().equals(IsComplementEnum.N.getValue())&& ObjectUtil.isNotEmpty(labelPrint.getPrintDate())){
            throw new CommonException("该标签不可补码", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        RuleLabel ruleLabel = ruleLabelMapper.selectById(labelPrint.getRuleLabelBo());

        if(ObjectUtil.isEmpty(ruleLabel)){
            throw new CommonException("查询不到规则模板", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();
        labelPrintDetail.setLastPrintUser(userName);
        labelPrintDetail.setLastPrintDate(date);
        labelPrintDetail.setPrintCount(labelPrintDetail.getPrintCount()+1);
        labelPrintDetailMapper.updateById(labelPrintDetail);

        LabelPrintLog labelPrintLog = new LabelPrintLog();
        labelPrintLog.setLabelPrintBo(labelPrint.getBo());
        labelPrintLog.setLabelPrintDetailBo(labelPrintDetail.getBo());
        labelPrintLog.setPrintDate(date);
        labelPrintLog.setPrintUser(UserUtils.getCurrentUser().getUserName());
        labelPrintLogMapper.insert(labelPrintLog);

        List<String> respLists = new ArrayList<>();
        if(labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())){
            List<Map<String,Object>> mapList = new ArrayList<>();
            Map<String,Object> map = JSONObject.parseObject(labelPrintDetail.getLabelParams());
            mapList.add(map);
            respLists = commonUtils.conversionUrl(labelService.batchCreatePdf(mapList,ruleLabel.getLabelBo()));
        }else if(labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.LODOP.getValue())){
            respLists.add(labelPrintDetail.getLabelParams());
        }

        return respLists;

    }

    /**
     * 根据类型检查条码放回对应BO和物料BO
     *
     * @param barCode     条码
     * @param elementType 条码类型
     * @return
     */
    @Override
    public ResponseData<CheckBarCodeVo> checkBarCode(String barCode, String elementType) {
        if(StrUtil.isBlank(barCode)||StrUtil.isBlank(elementType)){
            return ResponseData.error("条码和条码类型都不能为空");
        }
        ElementEnum elementEnum = ElementEnum.valueOf(elementType);
        if(BeanUtil.isEmpty(elementEnum)){
            return ResponseData.error("条码类型不存在");
        }
        QueryWrapper<LabelPrintDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.lambda().eq(LabelPrintDetail::getDetailCode,barCode);
        LabelPrintDetail printDetail = this.getOne(detailQueryWrapper, false);
        if(BeanUtil.isEmpty(printDetail)){
            return ResponseData.error("该条码不存在");
        }
        //判断类型是否一致
        LabelPrint labelPrint = labelPrintMapper.selectById(printDetail.getLabelPrintBo());
        if(!elementEnum.getType().equals(labelPrint.getElementType())){
            return ResponseData.error("该条码属于不正确");
        }
        CheckBarCodeVo checkBarCodeVo = new CheckBarCodeVo();
        checkBarCodeVo.setBo(printDetail.getBo());
        Set<String> ids = new HashSet<>();
        if(ElementConstant.ITEM.equals(elementEnum.getType())){
            checkBarCodeVo.setAmount(1);
            checkBarCodeVo.setItemBo(labelPrint.getElementBo());
            ids.add(labelPrint.getElementBo());
        }else {
            //箱条码处理，待表完善
        }
        List<Item> itemList = itemFeignService.getItemList(ids);
        if(CollectionUtil.isEmpty(itemList)){
            return ResponseData.error("没有获取到对应的物料数据");
        }
        Item item = itemList.get(0);
        checkBarCodeVo.setItem(item.getItem());
        checkBarCodeVo.setItemName(item.getItemName());
        checkBarCodeVo.setItemDesc(item.getItemDesc());
        return ResponseData.success(checkBarCodeVo);
    }

}
