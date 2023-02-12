package com.itl.mom.label.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.ItemLabelQueryDTO;
import com.itl.mom.label.api.dto.label.ItemLabelQueryUpDTO;
import com.itl.mom.label.api.service.label.LabelPrintService;
import com.itl.mom.label.api.vo.ItemLabelListVo;
import com.itl.mom.label.api.vo.LabelPrintVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author cjq
 * @Date 2021/10/13 11:39 上午
 * @Description 物料标签
 */
@Api(tags = "物料标签控制层")
@RequestMapping("/itemLabel")
@RestController
@AllArgsConstructor
@Validated
public class ItemLabelController {

    private LabelPrintService labelPrintService;

    @PostMapping("/pageList")
    @ApiOperation(value = "物料标签列表")
    public ResponseData<IPage<ItemLabelListVo>> getItemLabelPageList(@RequestBody @Validated ItemLabelQueryDTO itemLabelQueryDTO) throws CommonException {
        IPage<ItemLabelListVo> page = labelPrintService.getItemLabelPageList(itemLabelQueryDTO);
        return ResponseData.success(page);
    }

    @PostMapping("/hangup")
    @ApiOperation(value = "物料标签挂起")
    public ResponseData hangup(@RequestBody @Validated ItemLabelQueryUpDTO itemLabelQueryUpDTO) throws CommonException {
        labelPrintService.hangup(itemLabelQueryUpDTO);
        return ResponseData.success();
    }

}
