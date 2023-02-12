package com.itl.mes.andon.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.AndonSaveRepairCallBackDTO;
import com.itl.mes.andon.api.dto.RecordSaveDTO;
import com.itl.mes.andon.api.service.AndonTriggerService;
import com.itl.mes.andon.api.vo.AndonTriggerAndonVo;
import com.itl.mes.andon.api.vo.RecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @auth liuchenghao
 * @date 2020/12/23
 */
@RestController
@RequestMapping("/trigger")
@Api(tags = "安灯触发")
public class AndonTriggerController {

    @Autowired
    private AndonTriggerService andonTriggerService;

    /**
     *
     * @param map 当前用户的某个工位
     * @return 绑定的安灯信息
     */
    @PostMapping("/andonList")
    @ApiOperation(value = "查询当前用户绑定的安灯数据")
    public ResponseData<List<Map<String, Object>>> findAndonList(@RequestBody Map<String, String> map) {
        return ResponseData.success(andonTriggerService.findAndonList(Optional.ofNullable(map.get("stationBo")).orElseThrow(() -> new RuntimeException("未获取到工位!"))));
    }

    /**
     * 安灯触发
     */
    @PostMapping("/andonTrigger")
    @ApiOperation(value = "安灯触发")
    public ResponseData andonTrigger(@RequestBody RecordSaveDTO recordSaveDTO) {
        return ResponseData.success(andonTriggerService.saveRecord(recordSaveDTO));
    }

    @PostMapping("/uploadPicture")
    @ApiOperation(value = "上传图片")
    public ResponseData<String> uploadPicture(@RequestParam("file") MultipartFile[] files) {
        return ResponseData.success(andonTriggerService.upload(files));
    }

    @PostMapping("/uploadVideo")
    @ApiOperation(value = "上传video")
    public ResponseData<String> uploadVideo(@RequestParam("file") MultipartFile[] files) {
        return ResponseData.success(andonTriggerService.upload(files));
    }

    @GetMapping("/getRecord")
    @ApiOperation(value = "查询安灯异常信息")
    public ResponseData<RecordVo> getRecord(String andonBo) {
        return ResponseData.success(andonTriggerService.getRecord(andonBo));
    }

    @PostMapping("/saveRepair/callback")
    @ApiOperation(value = "保存维修工单回调")
    public ResponseData<Boolean> saveRepairCallBack(@RequestBody AndonSaveRepairCallBackDTO andonSaveRepairCallBackDTO) {
        return ResponseData.success(andonTriggerService.saveRepairCallBack(andonSaveRepairCallBackDTO));
    }
}
