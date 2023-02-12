package com.itl.mes.core.provider.utils.excel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.vo.ProductLineVo;
import com.itl.mes.core.provider.mapper.WorkShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备导入自定义校验
 * @author dengou
 * @date 2021/10/28
 */
@Component
public class ProducLineVerifyHandler implements IExcelVerifyHandler<ProductLineVo> {

    @Autowired
    private WorkShopMapper workShopMapper;



    @Override
    public ExcelVerifyHandlerResult verifyHandler(ProductLineVo productLineVo) {

        //设置默认验证为true
        ExcelVerifyHandlerResult excelVerifyHandlerResult = new ExcelVerifyHandlerResult(true);
        List<String> errorMsg = new ArrayList<>();

        if(StrUtil.isBlank(productLineVo.getProductLine())) {
            errorMsg.add("产线编码不能为空");
        }

        if(StrUtil.isBlank(productLineVo.getProductLineDesc())) {
            errorMsg.add("产线名称不能为空");
        }

        if(StrUtil.isBlank(productLineVo.getWorkShop())) {
            errorMsg.add("所属车间编码不能为空");
        }else {
            QueryWrapper<WorkShop> wrapper = new QueryWrapper<>();
            wrapper.eq("bo", new WorkShopHandleBO(UserUtils.getSite(),productLineVo.getWorkShop()).getBo());
            List<WorkShop> workShops = workShopMapper.selectList(wrapper);
            if(CollectionUtil.isEmpty(workShops)){
                errorMsg.add("车间" + productLineVo.getWorkShop() + "数据未维护");
            }

        }
        //拼接错误提示
        if(CollUtil.isNotEmpty(errorMsg)) {
            excelVerifyHandlerResult.setSuccess(false);
            excelVerifyHandlerResult.setMsg(CollUtil.join(errorMsg, ";"));
        }
        return excelVerifyHandlerResult;
    }

}
