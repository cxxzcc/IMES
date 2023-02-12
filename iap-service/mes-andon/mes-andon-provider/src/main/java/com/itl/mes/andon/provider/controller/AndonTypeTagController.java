package com.itl.mes.andon.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.AndonSaveDTO;
import com.itl.mes.andon.api.service.TagService;
import com.itl.mes.andon.api.vo.BoxForShowVo;
import com.itl.mes.andon.api.vo.BoxQueryVo;
import com.itl.mes.andon.api.vo.TagVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
@Api(tags = "安灯类型标识表")
public class AndonTypeTagController {
    private final Logger logger = LoggerFactory.getLogger(AndonTypeTagController.class);

    @Autowired
    private TagService tagService;

    @PostMapping("/save")
    @ApiOperation(value = "保存安灯类型标识数据")
    public ResponseData saveByTagVo(@RequestBody TagVo tagVo) throws CommonException {
        if (tagVo == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        tagService.saveByTagVo(tagVo);
        return ResponseData.success(true);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询安灯类型标识")
    @ApiOperationSupport(params = @DynamicParameters(name = "BoxRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面,默认1"),
            @DynamicParameter(name = "limit", value = "分页大小,默认100,可不填"),
            @DynamicParameter(name = "andonTypeTag", value = "安灯类型标识"),
            @DynamicParameter(name = "andonTypeTagName", value = "安灯类型标识名称"),
    }))
    public ResponseData<IPage<TagVo>> page(@RequestBody TagVo tagVo) throws CommonException {
        return ResponseData.success( tagService.queryPage(tagVo));
    }

}
