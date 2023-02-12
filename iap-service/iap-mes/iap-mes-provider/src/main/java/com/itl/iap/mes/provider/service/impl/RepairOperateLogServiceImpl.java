package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import com.itl.iap.mes.api.entity.RepairOperateLog;
import com.itl.iap.mes.api.service.RepairOperateLogService;
import com.itl.iap.mes.provider.mapper.RepairOperateLogMapper;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工单操作记录服务实现类
 *
 * @author dengou
 * @date 2021/10/25
 */
@Service
public class RepairOperateLogServiceImpl extends ServiceImpl<RepairOperateLogMapper, RepairOperateLog> implements RepairOperateLogService {

    @Autowired
    private UserService userService;


    @Override
    public RepairOperateLog getListByDeviceId(String deviceId) {
        Assert.valid(StrUtil.isBlank(deviceId), "设备id不能为空");

        List<RepairOperateLog> list = lambdaQuery().eq(RepairOperateLog::getDeviceId, deviceId)
                .orderByDesc(RepairOperateLog::getOperateTime)
                .list();
        RepairOperateLog repairOperateLog = list.stream().findFirst().orElse(new RepairOperateLog());
        return repairOperateLog;
    }

    @Override
    public Boolean add(RepairOperateLog repairOperateLog) {
        if (repairOperateLog.getOperateTime() == null) {
            repairOperateLog.setOperateTime(new Date());
        }
        if (repairOperateLog.getSite() == null) {
            repairOperateLog.setSite(UserUtils.getSite());
        }
        repairOperateLog.setId(null);
        return save(repairOperateLog);
    }

    @Override
    public List<RepairOperateLog> getListByOrderId(String orderId, String orderType, String site) {

        Assert.valid(StrUtil.isBlank(orderId), "工单id不能为空");
        Assert.valid(StrUtil.isBlank(orderType), "工单类型不能为空");
        Assert.valid(StrUtil.isBlank(site), "工厂id不能为空");

        List<RepairOperateLog> list = lambdaQuery().eq(RepairOperateLog::getOrderId, orderId)
                .eq(RepairOperateLog::getOrderType, orderType)
                .eq(RepairOperateLog::getSite, site)
                .orderByDesc(RepairOperateLog::getOperateTime)
                .list();
        if (CollUtil.isNotEmpty(list)) {
            //获取用户信息
            List<String> userIds = list.stream().map(RepairOperateLog::getOperateUserId).distinct().collect(Collectors.toList());
            ResponseData<List<IapSysUserTDto>> userListByIds = userService.getUserListByIds(userIds);
            Map<String, String> userIdPhoneMap = new HashMap<>(16);
            if (userListByIds.isSuccess()) {
                List<IapSysUserTDto> data = userListByIds.getData();
                if (CollUtil.isNotEmpty(data)) {
                    userIdPhoneMap.putAll(data.stream().collect(Collectors.toMap(IapSysUserTDto::getId, e -> e.getUserMobile() == null ? "" : e.getUserMobile())));
                }
            }
            list.forEach(e -> {
                e.setOperateTypeDesc(OperateTypeEnum.parseDescByCode(e.getOperateType()));
                e.setOrderTypeDesc(OrderTypeEnum.parseDescByCode(e.getOrderType()));
                if (userIdPhoneMap.containsKey(e.getOperateUserId())) {
                    e.setPhone(userIdPhoneMap.get(e.getOperateUserId()));
                }
            });
        }
        return list;
    }

    @Override
    public List<RepairOperateLog> getListByOrderNo(String orderNo, String orderType, String site) {

        Assert.valid(StrUtil.isBlank(orderNo), "工单编号不能为空");
        Assert.valid(StrUtil.isBlank(orderType), "工单类型不能为空");
        Assert.valid(StrUtil.isBlank(site), "工厂id不能为空");

        List<RepairOperateLog> list = lambdaQuery().eq(RepairOperateLog::getOrderNo, orderNo)
                .eq(RepairOperateLog::getOrderType, orderType)
                .eq(RepairOperateLog::getSite, site)
                .orderByDesc(RepairOperateLog::getOperateTime)
                .list();
        if (CollUtil.isNotEmpty(list)) {
            //获取用户信息
            List<String> userIds = list.stream().map(RepairOperateLog::getOperateUserId).distinct().collect(Collectors.toList());
            ResponseData<List<IapSysUserTDto>> userListByIds = userService.getUserListByIds(userIds);
            Map<String, String> userIdPhoneMap = new HashMap<>();
            if (userListByIds.isSuccess()) {
                List<IapSysUserTDto> data = userListByIds.getData();
                if (CollUtil.isNotEmpty(data)) {
                    userIdPhoneMap.putAll(data.stream().collect(Collectors.toMap(IapSysUserTDto::getId, IapSysUserTDto::getMobilePhone)));
                }
            }
            list.forEach(e -> {
                e.setOperateTypeDesc(OperateTypeEnum.parseDescByCode(e.getOperateType()));
                e.setOrderTypeDesc(OrderTypeEnum.parseDescByCode(e.getOrderType()));
                if (userIdPhoneMap.containsKey(e.getOperateUserId())) {
                    e.setPhone(userIdPhoneMap.get(e.getOperateUserId()));
                }
            });
        }
        return list;
    }

    @Override
    public RepairOperateLog getDetailById(String id) {
        RepairOperateLog repairOperateLog = getById(id);
        if (repairOperateLog != null) {
            repairOperateLog.setOperateType(OperateTypeEnum.parseDescByCode(repairOperateLog.getOperateType()));
            repairOperateLog.setOrderTypeDesc(OrderTypeEnum.parseDescByCode(repairOperateLog.getOrderType()));
        }
        return repairOperateLog;
    }
}
