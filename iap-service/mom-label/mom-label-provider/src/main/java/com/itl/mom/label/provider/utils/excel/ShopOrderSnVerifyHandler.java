package com.itl.mom.label.provider.utils.excel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mes.core.client.service.SnService;
import com.itl.mom.label.api.dto.label.ShopOrderSnImportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单条码导入校验
 * @author dengou
 * @date 2021/11/13
 */
@Component
public class ShopOrderSnVerifyHandler implements IExcelVerifyHandler<ShopOrderSnImportDto> {

    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private SnService snService;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ShopOrderSnImportDto obj) {
        //设置默认验证为true
        ExcelVerifyHandlerResult excelVerifyHandlerResult = new ExcelVerifyHandlerResult(true);
        List<String> errorMsg = new ArrayList<>();
        // 四大皆空
        if(StrUtil.isBlank(obj.getShopOrder()) && StrUtil.isBlank(obj.getSn()) && StrUtil.isBlank(obj.getRuleLabel()) && obj.getIsComplement() == null){

        }else{
            //参数是否为空
            if(StrUtil.isBlank(obj.getShopOrder())) {
                errorMsg.add("工单编号不能为空");
            }
            if(StrUtil.isBlank(obj.getSn())) {
                errorMsg.add("条码不能为空");
            }
            if(StrUtil.isBlank(obj.getRuleLabel())) {
                errorMsg.add("规则模板编号不能为空");
            }
            if(obj.getIsComplement() == null) {
                errorMsg.add("是否补打参数不能为空");
            }else if(obj.getIsComplement() != 1 && obj.getIsComplement() != 2) {
                errorMsg.add("是否补打参数错误，1代表补打，2代表不补打");
            }
        }

        //String site = UserUtils.getSite();
        //导入条码是否已存在
        /*Integer snCount = snService.lambdaQuery().eq(Sn::getBo, new SnHandleBO(site, obj.getSn()).getBo()).count();
        if(snCount > 0) {
            errorMsg.add("条码已存在");
        }*/
        /*if(!StrUtil.isBlank(obj.getSn())){
            Sn snObj = snService.getSnInfo(obj.getSn());
            if(snObj != null) {
                errorMsg.add("条码已存在");
            }
        }*/


        //工单是否已存在
        /*ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(site, obj.getShopOrder());
        Integer shopOrderCount = shopOrderService.lambdaQuery().eq(ShopOrder::getBo, shopOrderHandleBO.getBo()).count();
        if(shopOrderCount <= 0) {
            errorMsg.add("工单不存在");
        }*/

        // 工单判断
        /*if(!StrUtil.isBlank(obj.getShopOrder())) {
            String shopOrderNum = obj.getShopOrder();
            ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderNum);
            if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
                // throw new RuntimeException(shopOrderData.getMsg());
                errorMsg.add(shopOrderData.getMsg());
            }
            ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
            if(shopOrderFullVo == null){
                errorMsg.add("工单不存在！");
            }else if(shopOrderFullVo.getOrderQty() == null){
                errorMsg.add("工单数量为空！");
                // throw new RuntimeException("工单数量为空！");
            }else{
                Integer barCodeAmount = shopOrderFullVo.getOrderQty().intValue() - shopOrderFullVo.getLabelQty().intValue();
                if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(barCodeAmount)))<0){
                    errorMsg.add("打印数量超出工单可打印数量！");
                    // throw new RuntimeException("打印数量超出工单可打印数量！");
                }
            }
        }*/

        //拼接错误提示
        if(CollUtil.isNotEmpty(errorMsg)) {
            excelVerifyHandlerResult.setSuccess(false);
            excelVerifyHandlerResult.setMsg(CollUtil.join(errorMsg, ";"));
        }
        return excelVerifyHandlerResult;
    }
}
