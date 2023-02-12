package com.itl.iap.mes.provider.controller.report;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanDTO;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanQueryDTO;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.provider.mapper.*;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.client.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author cjq
 * @Date 2021/12/2 9:59 上午
 * @Description TODO
 */
@RestController
@RequestMapping("/report/mes")
@RequiredArgsConstructor
@Api(tags = "mes报表")
public class MesReport {

    private final CheckExecuteMapper checkExecuteMapper;
    private final CheckPlanUserMapper checkPlanUserMapper;
    private final DataCollectionMapper dataCollectionMapper;
    private final CheckExecuteItemMapper checkExecuteItemMapper;
    private final DataCollectionItemListingMapper dataCollectionItemListingMapper;
    private final DictService dictService;
    private final DeviceService deviceService;
    private final UserService userService;

    @ApiOperation(value = "点检工单报表")
    @PostMapping(value = "/getCheckExecuteList")
    public ResponseData<List<CheckExecuteAndPlanDTO>> getCheckExecuteList(@RequestBody CheckExecuteAndPlanQueryDTO checkExecuteAndPlanQueryDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t.siteId", checkExecuteAndPlanQueryDTO.getSite());
        if (StrUtil.isNotBlank(checkExecuteAndPlanQueryDTO.getOperaStartTime())
                && StrUtil.isNotBlank(checkExecuteAndPlanQueryDTO.getOperaEndTime())) {
            queryWrapper.apply("not (t.operaStartTime>{0} or t.operaEndTime < {1})", checkExecuteAndPlanQueryDTO.getOperaEndTime(), checkExecuteAndPlanQueryDTO.getOperaStartTime());
        }
        List<CheckExecuteAndPlanDTO> list = checkExecuteMapper.getCheckExecuteList(queryWrapper);
        List<String> checkIds = list.stream().map(CheckExecuteAndPlanDTO::getId).collect(Collectors.toList());
        //状态信息
        Map<String, String> dictMap = getDictMap("NX202012147234");
        //设备信息
        DeviceConditionDto conditionDto = new DeviceConditionDto();
        List<String> deviceIdList = list.stream().map(CheckExecuteAndPlanDTO::getDeviceId).distinct().collect(Collectors.toList());
        conditionDto.setBoList(deviceIdList);
        Map<String, DevicePlusVo> deviceMap = getDeviceMap(conditionDto);
        //点检人信息
        List<String> planIdList = list.stream().map(CheckExecuteAndPlanDTO::getCheckPlanId).distinct().collect(Collectors.toList());
        List<CheckPlanUser> checkPlanUsers = checkPlanUserMapper.selectList(Wrappers.<CheckPlanUser>lambdaQuery().in(CheckPlanUser::getCheckId, planIdList));
        List<String> userNames = checkPlanUsers.stream().map(CheckPlanUser::getCheckUserId).distinct().collect(Collectors.toList());
        Map<String, IapSysUserT> userMap = getUserMap(userNames);
        Map<String, List<CheckPlanUser>> checkUserMap = checkPlanUsers.stream().map(v -> {
                    if (userMap.containsKey(v.getCheckUserId())) {
                        v.setCheckUserName(userMap.get(v.getCheckUserId()).getRealName());
                    }
                    return v;
                }
        ).collect(Collectors.groupingBy(CheckPlanUser::getCheckId));
        //数据收集组
        List<String> dataCollectionIds = list.stream().map(CheckExecuteAndPlanDTO::getDataCollectionId).collect(Collectors.toList());
        List<DataCollection> dataCollections = dataCollectionMapper.selectList(Wrappers.<DataCollection>lambdaQuery().in(DataCollection::getId, dataCollectionIds));
        Map<String, DataCollection> dataCollectionMap = dataCollections.stream().collect(Collectors.toMap(DataCollection::getId, Function.identity()));
        for (CheckExecuteAndPlanDTO checkExecuteAndPlanDTO : list) {
            if (dictMap != null) {
                checkExecuteAndPlanDTO.setStateName(dictMap.get(checkExecuteAndPlanDTO.getState()));
            }
            if (deviceMap != null) {
                DevicePlusVo devicePlusVo = deviceMap.get(checkExecuteAndPlanDTO.getDeviceId());
                checkExecuteAndPlanDTO.setDevice(devicePlusVo.getDevice());
                checkExecuteAndPlanDTO.setDeviceName(devicePlusVo.getDeviceName());
                checkExecuteAndPlanDTO.setDeviceType(devicePlusVo.getDeviceType());
            }
            String checkPlanId = checkExecuteAndPlanDTO.getCheckPlanId();
            if (checkUserMap.containsKey(checkPlanId)) {
                String userName = checkUserMap.get(checkPlanId).stream().map(CheckPlanUser::getCheckUserName).collect(Collectors.joining(","));
                checkExecuteAndPlanDTO.setCheckUserName(userName);
            }
            if (dataCollectionMap.containsKey(checkExecuteAndPlanDTO.getDataCollectionId())) {
                checkExecuteAndPlanDTO.setDataCollectionName(dataCollectionMap.get(checkExecuteAndPlanDTO.getDataCollectionId()).getName());
            }
        }
        return ResponseData.success(list);
    }


    private Map<String, IapSysUserT> getUserMap(List<String> userNames) {
        ResponseData<List<IapSysUserT>> userList = userService.getUserList(userNames);
        Map<String, IapSysUserT> userTMap = null;
        if (userList != null && ResponseData.success().getCode().equals(userList.getCode())) {
            List<IapSysUserT> data = userList.getData();
            if (data != null) {
                userTMap = data.stream().collect(Collectors.toMap(IapSysUserT::getUserName, Function.identity()));
            }
        }
        return userTMap;
    }


    /**
     * 获取设备map
     *
     * @param conditionDto
     * @return
     */
    private Map<String, DevicePlusVo> getDeviceMap(DeviceConditionDto conditionDto) {
        ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(conditionDto);
        Map<String, DevicePlusVo> collect = null;
        if (dataByCondition != null && ResponseData.success().getCode().equals(dataByCondition.getCode())) {
            List<DevicePlusVo> data = dataByCondition.getData();
            if (data != null) {
                collect = data.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            }
        }
        return collect;
    }


    /**
     * 字典编号获取子项
     *
     * @param bh
     * @return
     */
    private Map<String, String> getDictMap(String bh) {
        ResponseData<Map<String, String>> response = dictService.getDictItemMapByParentCode(bh);
        Map<String, String> dictMap = null;
        if (response != null && ResponseData.success().getCode().equals(response.getCode())) {
            dictMap = response.getData();
        }
        return dictMap;
    }


}
