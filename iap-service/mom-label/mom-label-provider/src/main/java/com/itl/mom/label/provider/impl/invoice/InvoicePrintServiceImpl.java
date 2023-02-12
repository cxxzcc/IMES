package com.itl.mom.label.provider.impl.invoice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mom.label.api.dto.invoice.InvoicePrintDto;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabel;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabelDetail;
import com.itl.mom.label.api.service.InvoicePrintService;
import com.itl.mom.label.api.service.label.LabelService;
import com.itl.mom.label.provider.constant.InvoiceTypeConstant;
import com.itl.mom.label.provider.feign.*;
import com.itl.mom.label.provider.mapper.ruleLabel.RuleLabelDetailMapper;
import com.itl.mom.label.provider.mapper.ruleLabel.RuleLabelMapper;
import com.itl.mom.label.provider.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuchenghao
 * @date 2021/5/13 9:59
 */
@Service
public class InvoicePrintServiceImpl implements InvoicePrintService {

    @Autowired
    RuleLabelMapper ruleLabelMapper;

    @Autowired
    RuleLabelDetailMapper ruleLabelDetailMapper;

    @Autowired
    AsnService asnService;

    @Autowired
    ShipmentOrderService shipmentOrderService;

    @Autowired
    InWarehouseService inWarehouseService;

    @Autowired
    CompletedInstockService completedInstockService;

    @Autowired
    CompletedReturnService completedReturnService;

    @Autowired
    WmInventoryBillService wmInventoryBillService;

    @Autowired
    SalesReturnService salesReturnService;

    @Autowired
    WmTransferBillService wmTransferBillService;

    @Autowired
    VendorReturnService vendorReturnService;

    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Autowired
    WmScrapBillService wmScrapBillService;

    @Autowired
    WmTransportBillService wmTransportBillService;


    @Autowired
    LabelService labelService;

    @Autowired
    CommonUtils commonUtils;

    @Autowired
    SendOrReturnItemParamService sendOrReturnItemParamService;

    @Autowired
    ReceiveReturnPlatformService receiveReturnPlatformService;

    @Override
    public List<String> invoicePrint(InvoicePrintDto invoicePrintDto) throws CommonException {


        QueryWrapper<RuleLabel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DOCUMENT_TYPE",invoicePrintDto.getInvoiceType());
        queryWrapper.eq("SITE", UserUtils.getSite());
        List<RuleLabel> ruleLabels = ruleLabelMapper.selectList(queryWrapper);
        if (ruleLabels == null || ruleLabels.size() != 1) {
            throw new RuntimeException("无法确定该单据的规则模板!");
        }
        RuleLabel ruleLabel = ruleLabels.get(0);

        QueryWrapper<RuleLabelDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("IRL_BO",ruleLabel.getBo());
        List<RuleLabelDetail> ruleLabelDetails = ruleLabelDetailMapper.selectList(wrapper);

        List<RuleLabelDetail> mainRuleLabelDetails = new ArrayList<>();
        List<RuleLabelDetail> subRuleLabelDetails = new ArrayList<>();
        ruleLabelDetails.forEach(ruleLabelDetail -> {

            if("6".equals(ruleLabelDetail.getLabelParamType())){
                subRuleLabelDetails.add(ruleLabelDetail);
            }else {
                mainRuleLabelDetails.add(ruleLabelDetail);
            }
        });

        List<Map<String, Object>> mapList = new ArrayList<>();

        switch (invoicePrintDto.getInvoiceType()){
            case InvoiceTypeConstant.ASN:
                mapList = asnService.getInvoiceAsn(invoicePrintDto.getInvoiceNos( ));
                break;
            case InvoiceTypeConstant.COMPLETED_INSTOCK:
                mapList = completedInstockService.getInvoiceInstock(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.COMPLETED_RETURN:
                mapList = completedReturnService.getInvoiceReturn(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.IN_WAREHOUSE:
                mapList = inWarehouseService.getInvoiceInWarehouse(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.INVENTORY_BILL:
                mapList = wmInventoryBillService.getInvoiceInventoryBill(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.RETURN_ITEM:
                mapList = sendOrReturnItemParamService.getInvoiceReturnItem(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.SALES_OUT:
                mapList = sendOrReturnItemParamService.getInvoiceSalesOut(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.SALES_RETURN:
                mapList = salesReturnService.getInvoiceSalesReturn(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.SCRAP_BILL:
                mapList = wmScrapBillService.getInvoiceScrapBill(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.SEND_ITEM:
                mapList = sendOrReturnItemParamService.getInvoiceSendItem(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.TRANSFER_BILL:
                mapList = wmTransferBillService.getInvoiceTransferBill(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.SHIPMENT_ORDER:
                mapList = shipmentOrderService.getInvoiceShipmentOrder(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.TRANSPORT_BILL:
                mapList = wmTransportBillService.getInvoiceTransportBill(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.PURCHASE_ORDER:
                mapList = purchaseOrderService.getInvoicePurchaseOrder(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.VENDOR_RETURN:
                mapList = vendorReturnService.getInvoiceVendorReturn(invoicePrintDto.getInvoiceNos());
                break;
            case InvoiceTypeConstant.RECEIVE_RETURN_PLATFORM:
                mapList = receiveReturnPlatformService.getInvoiceReceiveReturn(invoicePrintDto.getInvoiceNos());
                break;
            default:break;
        }
        //查询模板的详细数据和map进行匹配，如果key值与字段值对应，不做改动，不对应则替换，如果主表需要显示子表数据
        if(CollUtil.isEmpty(mapList)){
            throw new CommonException("查询不到单据数据!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        for(Map<String,Object> map : mapList){

            Object listObj = Objects.requireNonNull(map.get("listDataSource"), "未获取到明细信息!");
            List<Map<String,Object>> subList =(List<Map<String,Object>>) listObj;
            List<RuleLabelDetail> subChangeMain = new ArrayList<>();
            for(RuleLabelDetail ruleLabelDetail: mainRuleLabelDetails){
                if(map.containsKey(ruleLabelDetail.getRuleVal())){
                    map.put(ruleLabelDetail.getTemplateArg(),map.remove(ruleLabelDetail.getRuleVal()));
                }else {
                    //当标签主数据需要显示子表数据时，记录该字段
                    if(ruleLabelDetail.getIsDetail().equals("Y")){
                        subChangeMain.add(ruleLabelDetail);
                    }
                }
            }

            for(Map<String,Object> subMap : subList){

                //当主标签需显示子表数据，遍历子表数据，用逗号分隔展示
                if(CollUtil.isNotEmpty(subChangeMain)){
                    subChangeMain.forEach(ruleLabelDetail -> {
                        if(map.containsKey(ruleLabelDetail.getTemplateArg())){
                            String val = (String)subMap.get(ruleLabelDetail.getRuleVal());
                            map.put(ruleLabelDetail.getTemplateArg(),val+","+map.get(ruleLabelDetail.getTemplateArg()));
                        }else {
                            if(ObjectUtil.isNotEmpty(ruleLabelDetail.getRuleVal())){
                                map.put(ruleLabelDetail.getTemplateArg(),subMap.get(ruleLabelDetail.getRuleVal()));
                            }
                        }
                    });

                }
                //遍历子数据然后进行key转换
                subRuleLabelDetails.forEach(ruleLabelDetail -> {
                    if(subMap.containsKey(ruleLabelDetail.getRuleVal())){
                        subMap.put(ruleLabelDetail.getTemplateArg(),subMap.remove(ruleLabelDetail.getRuleVal()));
                    }
                });

            }

        }

        return  commonUtils.conversionUrl(labelService.batchCreatePdf(mapList, ruleLabel.getLabelBo()));
    }
}
