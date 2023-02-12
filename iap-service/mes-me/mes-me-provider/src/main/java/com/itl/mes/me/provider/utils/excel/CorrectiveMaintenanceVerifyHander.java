package com.itl.mes.me.provider.utils.excel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.mes.me.api.entity.ErrorType;
import com.itl.mes.me.api.entity.MaintenanceMethod;
import com.itl.mes.me.api.vo.CorrectiveMaintenanceVo;
import com.itl.mes.me.provider.mapper.MaintenanceMethodMapper;
import com.itl.mes.me.provider.service.impl.ErrorTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 维修措施导入模板校验
 *
 * @author GKL
 * @date 2021/11/12 - 14:30
 * @since 2021/11/12 - 14:30 星期五 by GKL
 */
@Component
@RequiredArgsConstructor
public class CorrectiveMaintenanceVerifyHander implements IExcelVerifyHandler<CorrectiveMaintenanceVo> {

    private final ErrorTypeServiceImpl errorTypeService;

    private final MaintenanceMethodMapper maintenanceMethodMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(CorrectiveMaintenanceVo obj) {
        ExcelVerifyHandlerResult excelVerifyHandlerResult = new ExcelVerifyHandlerResult(true);
        String site = UserUtils.getSite();

        // String site = "1040";
       // site = org.apache.commons.lang3.StringUtils.isEmpty(site) ? site : "1040";
        List<String> errorMsg = new ArrayList<>();
        String code = obj.getCode();
        if (StringUtils.isEmpty(code)) {
            errorMsg.add("维修编码不能为空");
        }else{

            if(StringUtils.isEmpty(site)){
                errorMsg.add("获取工厂失败");
            }else{
                LambdaQueryWrapper<MaintenanceMethod> queryWrapper = new QueryWrapper<MaintenanceMethod>().lambda()
                        .eq(MaintenanceMethod::getCode, code)
                        .eq(MaintenanceMethod::getSite,site);
                List<MaintenanceMethod> maintenanceMethods = maintenanceMethodMapper.selectList(queryWrapper);
                if(Boolean.FALSE.equals(CollectionUtils.isEmpty(maintenanceMethods))){
                    errorMsg.add("维修编码已存在");
                }

            }
        }
        //异常类型
        String errorTypeCode = obj.getErrorTypeCode();
        if (StringUtils.isEmpty(errorTypeCode)) {
            errorMsg.add("维修类型不能为空");
        } else {
            List<ErrorType> tree = errorTypeService.getErrorType(site);
            if (CollectionUtils.isEmpty(tree)) {
                errorMsg.add("异常分类表数据不存在");

            } else {
                List<ErrorType> collect = tree.stream().filter(x -> errorTypeCode.equals(x.getErrorCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(collect)) {
                    errorMsg.add("异常类型不存在异常分类表数据");
                }
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
