package com.itl.mes.core.provider.controller.report;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.constants.DateScopeEnum;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.LocalDateTimeUtils;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.constant.CheckResultEnum;
import com.itl.mes.core.api.dto.ProductCheckoutDTO;
import com.itl.mes.core.api.dto.ProductCheckoutDetailDTO;
import com.itl.mes.core.api.vo.ProductCheckoutDetailVO;
import com.itl.mes.core.api.vo.ProductCheckoutVO;
import com.itl.mes.core.provider.mapper.CollectionRecordMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
@RequestMapping("/report/md")
@RequiredArgsConstructor
@Api(tags = "md报表")
public class MdReport {

    private final DictService dictService;
    private final UserService userService;
    private final CollectionRecordMapper collectionRecordMapper;

    @ApiOperation(value = "产品检验统计报表")
    @PostMapping(value = "/getProductCheckoutList")
    public ResponseData<List<ProductCheckoutVO>> getProductCheckoutList(@RequestBody ProductCheckoutDTO productCheckoutDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(productCheckoutDTO);
        queryWrapper.in("b.result", Lists.newArrayList(CheckResultEnum.OK.getCode(), CheckResultEnum.NG.getCode()));
        queryWrapper.isNotNull("b.project_name");
        if (StrUtil.isBlank(productCheckoutDTO.getStartTime()) && StrUtil.isBlank(productCheckoutDTO.getEndTime())) {
            LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(DateScopeEnum.WEEK.getCode());
            queryWrapper.between("b.create_time", dateTimeScope.getStartTime(), dateTimeScope.getEndTime());
        }
        if (StrUtil.isNotBlank(productCheckoutDTO.getStartTime())) {
            queryWrapper.gt("b.create_time", productCheckoutDTO.getStartTime());
        }
        if (StrUtil.isNotBlank(productCheckoutDTO.getEndTime())) {
            queryWrapper.lt("b.create_time", productCheckoutDTO.getEndTime());
        }
        List<ProductCheckoutVO> productCheckoutList = collectionRecordMapper.getProductCheckoutList(queryWrapper);
        return ResponseData.success(productCheckoutList);
    }

    @ApiOperation(value = "产品检验统计详情报表")
    @PostMapping(value = "/getProductCheckoutDetailList")
    public ResponseData<List<ProductCheckoutDetailVO>> getProductCheckoutDetailList(@RequestBody ProductCheckoutDetailDTO productCheckoutDetailDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(productCheckoutDetailDTO);
        queryWrapper.isNotNull("b.project_name");
        List<ProductCheckoutDetailVO> productCheckoutDetailList = collectionRecordMapper.getProductCheckoutDetailList(queryWrapper);
        return ResponseData.success(productCheckoutDetailList);
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
