package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.service.NcCodeService;
import com.itl.mes.core.api.vo.NcCodeVo;
import com.itl.mes.core.api.vo.NcGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author space
 * @since 2019-05-24
 */
@RestController
@RequestMapping("/ncCodes")
@Api(tags = " 不合格代码表" )
public class NcCodeController {
    private final Logger logger = LoggerFactory.getLogger(NcCodeController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public NcCodeService ncCodeService;


    /**
     * 根据List<BO>查询不合格代码对象
     * 结果集类型List<NcCode>
     * /ncCodes/selectListNcCode/byListBo
     */
    @PostMapping("/selectListNcCode/byListBo")
    public ResponseData<List<NcCode>> selectByListBo(@RequestBody List<String> boList){
        List<NcCode> ncCodes = (List<NcCode>) ncCodeService.listByIds(boList);
        return ResponseData.success(ncCodes);
    }

    /**
     * 不良代码插入
     */
    @PostMapping( "/save" )
    @ApiOperation(value="保存不良代码数据")
    public ResponseData<NcCodeVo> saveNcCode(@RequestBody NcCodeVo ncCodeVo) throws CommonException {
        if(ncCodeVo==null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ncCodeService.saveNcCode(ncCodeVo);
        NcCodeVo CodeVo =  ncCodeService.getNcCodeVoByNcCode(ncCodeVo.getNcCode());
        return ResponseData.success(CodeVo);
    }

    @GetMapping("/query")
    @ApiOperation(value="查询不良代码数据")
    public ResponseData<NcCodeVo> selectNcCodeByMap(String ncCode) throws CommonException {
        if(StrUtil.isBlank(ncCode))throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        NcCodeVo ncCodeVo =  ncCodeService.getNcCodeVoByNcCode(ncCode);
        return ResponseData.success(ncCodeVo);
    }


    @GetMapping("/delete")
    @ApiOperation(value="删除不良代码数据")
    public  ResponseData<String> deleteNcCode(String ncCode ,String modifyDate) throws CommonException {
        if(StrUtil.isBlank(ncCode)) throw new CommonException("不良代码不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        ncCodeService.deleteNcCode(ncCode, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success( "success" );
    }

    @GetMapping("/getNcGroupVoList")
    @ApiOperation(value="查询前500条不良代码组页面初始化用")
    public ResponseData<List<NcGroupVo>> getNcGroupVoList() throws CommonException {
        List<NcGroupVo> ncGroupVoList = ncCodeService.getNcGroupVoList();
        return ResponseData.success(ncGroupVoList);
    }

    /**
     * 根据不合格代码组bo列表查询不合格代码组code集合
     * */
    @PostMapping("/getNcCodeByNcgBos")
    @ApiOperation(value="根据不合格代码组bo列表查询不合格代码组code集合")
    public ResponseData<List<NcCodeVo>> getNcCodeByNcgBos(@RequestBody List<String> ncgBos) {
        return ResponseData.success(ncCodeService.getNcCodeByNcgBos(ncgBos, UserUtils.getSite()));
    }


}