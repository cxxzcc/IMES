package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.service.SiteUsrService;
import com.itl.mes.core.api.vo.SiteUsrRequestVo;
import com.itl.mes.core.api.vo.SiteUsrResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author space
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/siteUsrs")
@Api(tags = " 工厂用户关系维护" )
public class SiteUsrController {
    private final Logger logger = LoggerFactory.getLogger(SiteUsrController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public SiteUsrService siteUsrService;

    /**
    * 用户查询
    *
    * @param usr
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value="通过用户查询数据")
    public ResponseData<SiteUsrResponseVo> getSiteUsrByUsr(String usr ) throws CommonException {
        if( StrUtil.isBlank( usr ) ){
            throw new CommonException( "用户不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(siteUsrService.getSiteUsrByUsr( usr ));
    }

    @PostMapping("/save")
    @ApiOperation(value="保存用户工厂对应关系")
    public ResponseData<SiteUsrResponseVo> saveUsrSite( @RequestBody SiteUsrRequestVo siteUsrRequestVo ) throws CommonException {
        if( StrUtil.isBlank( siteUsrRequestVo.getUsr() ) ){
            throw new CommonException( "用户不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        if( StrUtil.isBlank( siteUsrRequestVo.getDefaultSites() ) ){
            throw new CommonException( "默认工厂为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        siteUsrService.saveUsrSite( siteUsrRequestVo );
        return ResponseData.success(siteUsrService.getSiteUsrByUsr( siteUsrRequestVo.getUsr() ));
    }

    @GetMapping("/delete")
    @ApiOperation(value="删除用户工厂对应关系")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "usr", value = "用户", dataType = "string", paramType = "query" ),
    })
    public ResponseData<String> deleteUsrSite( String usr) throws CommonException {
        if( StrUtil.isBlank( usr ) ){
            throw new CommonException( "用户不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        siteUsrService.deleteUsrSite(usr);
        return ResponseData.success("success" );
    }

}
