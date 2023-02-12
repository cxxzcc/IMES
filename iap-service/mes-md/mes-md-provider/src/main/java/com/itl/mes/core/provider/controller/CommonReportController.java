package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.mes.core.api.service.CommonReportService;
import com.itl.mes.core.api.service.LogicService;
import com.itl.mes.core.api.vo.QueryLogicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monopy/commonReport")
@Api(tags = "报表通用入口")
public class CommonReportController {

    private final Logger logger = LoggerFactory.getLogger(CommonReportController.class);

    @Autowired
    private CommonReportService commonReportService;

    @Autowired
    public LogicService logicService;



    @PostMapping("/exportQuery")
    @ApiOperation(value="查询数据")
    public ResponseData< List<Map<String,Object>> > query(@RequestBody QueryLogicVo queryLogicVo ) throws CommonException {

        if( queryLogicVo==null ){
            throw new CommonException( "查询参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        StrNotNull.validateNotNull( queryLogicVo.getLogicNo(), "逻辑编号不能为空" );
        String sql = "";
        if( StrUtil.isBlank( queryLogicVo.getVersion() ) ){
            sql = logicService.getCurrentLogicNoContent( queryLogicVo.getLogicNo() );
        }else{
            sql = logicService.getLogicNoAndVersionContent( queryLogicVo.getLogicNo(), queryLogicVo.getVersion() );
        }
        Map<String,Object> paramsMap = new HashMap<>();
        if( queryLogicVo.getParams()!=null ){
            paramsMap.putAll( queryLogicVo.getParams() );
        }
        return ResponseData.success( commonReportService.selectManualSql( sql, paramsMap ) );
    }

}
