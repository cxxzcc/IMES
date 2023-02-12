package com.itl.mes.core.provider.utils.excel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.TeamHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.entity.Team;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.service.ProductLineService;
import com.itl.mes.core.api.vo.EmployeeVo;
import com.itl.mes.core.api.vo.ProductLineVo;
import com.itl.mes.core.api.vo.TeamVo;
import com.itl.mes.core.provider.mapper.TeamMapper;
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
public class TeamVoVerifyHandler implements IExcelVerifyHandler<TeamVo> {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private ProductLineService productLineService;
    @Override
    public ExcelVerifyHandlerResult verifyHandler(TeamVo teamVo) {

        //设置默认验证为true
        ExcelVerifyHandlerResult excelVerifyHandlerResult = new ExcelVerifyHandlerResult(true);
        List<String> errorMsg = new ArrayList<>();

        if(StrUtil.isBlank(teamVo.getTeam())) {
            errorMsg.add("班组编码不能为空");
        }else {
            Team teamEntity = teamMapper.selectById(new TeamHandleBO(UserUtils.getSite(),teamVo.getTeam()).getBo());
         if(teamEntity != null){
             errorMsg.add("班组编码重复");
         }
        }

        if(StrUtil.isBlank(teamVo.getProductLine())) {
            errorMsg.add("产线不能为空");
        }else {
            ProductLine productLine = productLineService.selectByProductLine(teamVo.getProductLine());
             if(productLine == null){
                 errorMsg.add("产线不存在，未维护");
             }
        }

        if(StrUtil.isBlank(teamVo.getLeader())) {
            errorMsg.add("班组长不能为空");
        }else {
            EmployeeVo employee = teamMapper.getEmployee(teamVo.getLeader());
            if(employee == null ){
                errorMsg.add("员工+teamVo.getLeader()+未维护");
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
