package com.itl.iap.notice.provider.controller;

import cn.hutool.core.collection.CollUtil;
import com.itl.iap.common.base.response.ResponseData;

import com.itl.iap.notice.api.entity.PushTargetPerson;
import com.itl.iap.notice.api.service.IPushTargetPersonService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备消息中心
 */
@RestController
@RequestMapping("/pushTargetPerson")
public class MsgInStationPushTargetPersonController {

    @Autowired
    private IPushTargetPersonService pushTargetPersonService;

    // 中间表 PushTargetPerson
    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "配置消息接收人")
    public ResponseData<Boolean> layoutTargetPerson(@RequestBody List<PushTargetPerson> maps) {
        List<PushTargetPerson> updateParams = maps.stream().filter(x -> !StringUtils.isEmpty(x.getId())).collect(Collectors.toList());
        List<PushTargetPerson> saveParams = maps.stream().filter(x -> StringUtils.isEmpty(x.getId())).collect(Collectors.toList());

        // 批量添加
        if (CollUtil.isNotEmpty(saveParams)) {
            boolean s = pushTargetPersonService.saveBatch(saveParams);
        }

        // 更新
        if (CollUtil.isNotEmpty(updateParams)) {
            boolean u = pushTargetPersonService.updateBatchById(updateParams);
        }
        return ResponseData.success(true);
    }

    // 查询接口: 模板表,User表 多对多。根据中间表关联查询,返回给前端。或者发送消息时用来获取目标用户
    // 在PC端每个业务模块 所需要的消息模板都会配置。中间表携带模板与user的关系
    @PostMapping("/getTargetUser")
    @ApiOperation(value = "查询")
    public void getTargetPersonTemplate(@RequestBody List<String> templateIds) {


    }


}
