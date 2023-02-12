package com.itl.mes.me.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.LabelPrintBarCodeDto;
import com.itl.mes.me.api.dto.LabelPrintDetailQueryDto;
import com.itl.mes.me.api.dto.LabelPrintQueryDto;
import com.itl.mes.me.api.dto.LabelPrintSaveDto;
import com.itl.mes.me.api.service.LabelPrintDetailService;
import com.itl.mes.me.api.service.LabelPrintLogService;
import com.itl.mes.me.api.service.LabelPrintService;
import com.itl.mes.me.api.vo.LabelPrintDetailVo;
import com.itl.mes.me.api.vo.LabelPrintLogVo;
import com.itl.mes.me.api.vo.LabelPrintVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2021/1/26 15:34
 */
@Api(tags = "标签打印控制层")
@RestController
@RequestMapping("/labelPrintRange")
public class LabelPrintController {

    @Autowired
    LabelPrintService labelPrintService;

    @Autowired
    LabelPrintDetailService labelPrintDetailService;

    @Autowired
    LabelPrintLogService labelPrintLogService;


    @PostMapping("/addLabelPrint")
    @ApiOperation(value = "添加标签打印任务")
    public ResponseData addLabelPrint(@RequestBody LabelPrintSaveDto labelPrintSaveDto) throws CommonException {
        labelPrintService.addLabelPrint(labelPrintSaveDto);
        return ResponseData.success();
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询标签打印集合")
    public ResponseData<IPage<LabelPrintVo>> findList(@RequestBody LabelPrintQueryDto labelPrintQueryDto) throws CommonException {

        return ResponseData.success(labelPrintService.findList(labelPrintQueryDto));
    }



    @PostMapping("/barCodePrint")
    @ApiOperation(value = "条码打印")
    public ResponseData barCodePrint(@RequestBody LabelPrintBarCodeDto barCodeDto) {

        try {
            return ResponseData.success(labelPrintService.barCodePrint(barCodeDto));
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/detailList")
    @ApiOperation(value = "查询标签详细打印集合")
    public ResponseData<IPage<LabelPrintDetailVo>> findDetailList(@RequestBody LabelPrintDetailQueryDto labelPrintRangeDetailQueryDto) throws CommonException {

        return ResponseData.success(labelPrintDetailService.findList(labelPrintRangeDetailQueryDto));
    }


    @PostMapping("/barCodeDetailPrint")
    @ApiOperation(value = "条码详细打印")
    public ResponseData barCodeDetailPrint(@RequestBody String detailBo) {

        try {
            return ResponseData.success(labelPrintDetailService.barCodeDetailPrint(detailBo));
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }

    }



    @PostMapping("/labelPrintDetailLogList")
    @ApiOperation(value = "条码详细打印日志信息")
    public ResponseData<List<LabelPrintLogVo>> labelPrintDetailLogList(@RequestBody String labelPrintDetailBo){

        return ResponseData.success(labelPrintLogService.findDetailList(labelPrintDetailBo));
    }

}
