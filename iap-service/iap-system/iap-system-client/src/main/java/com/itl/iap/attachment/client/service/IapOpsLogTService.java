package com.itl.iap.attachment.client.service;

import com.itl.iap.attachment.client.config.FallBackConfig;
import com.itl.iap.attachment.client.service.impl.IapOpsLogTServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户服务远程调用
 *
 * @author chenjx1
 * @date 2021-12-31
 * @since jdk 1.8
 */
@FeignClient(value = "iap-system-provider", fallbackFactory = IapOpsLogTServiceImpl.class, configuration = FallBackConfig.class)
public interface IapOpsLogTService {
    @PostMapping("/iapOpsLogT/add")
    @ApiOperation(value = "新增记录", notes = "新增登陆记录")
    ResponseData add(@RequestBody IapOpsLogT iapOpsLogT) ;

    @PostMapping("/iapOpsLogT/delete")
    @ApiOperation(value = "根据日志ID删除记录", notes = "根据日志ID删除记录")
    ResponseData delete(@RequestBody IapOpsLogT iapOpsLogT) ;

    @PostMapping("/iapOpsLogT/update")
    @ApiOperation(value = "根据日志ID修改记录", notes = "根据日志ID修改记录")
    ResponseData update(@RequestBody IapOpsLogT iapOpsLogT) ;

    @PostMapping("/iapOpsLogT/query")
    @ApiOperation(value = "分页查询全部Ops日志", notes = "分页查询全部Ops日志")
    ResponseData queryRecord(@RequestBody IapOpsLogTDto iapOpsLogTDto) ;

    @PostMapping("/iapOpsLogT/pageQueryTypeInterface")
    @ApiOperation(value = "分页查询系统接口日志", notes = "分页查询系统接口日志")
    ResponseData pageQueryTypeInterface(@RequestBody IapOpsLogTDto iapOpsLogDto) ;

    @PostMapping("/iapOpsLogT/pageQueryException")
    @ApiOperation(value = "分页查询异常日志", notes = "分页查询异常日志")
    ResponseData pageQueryException(@RequestBody IapOpsLogTDto iapOpsLogDto) ;

    @PostMapping("/iapOpsLogT/pageQueryInteractive")
    @ApiOperation(value = "分页查询交互接口日志", notes = "分页查询交互接口日志")
    ResponseData pageQueryInteractive(@RequestBody IapOpsLogTDto iapOpsLogDto) ;

    @PostMapping("/iapOpsLogT/listQueryInteractive")
    @ApiOperation(value = "交互接口日志List", notes = "交互接口日志List")
    List<IapOpsLogT> listQueryInteractive(@RequestBody IapOpsLogT iapOpsLogT) ;

    @PostMapping("/iapOpsLogT/getByIds")
    @ApiOperation(value = "交互接口日志List", notes = "交互接口日志List")
    List<IapOpsLogT> getByIds(@RequestParam List<String> ids);

    @ApiOperation("交互接口推送")
    @PostMapping("/iapOpsLogT/call")
    ResponseData call(@RequestBody List<String> ids);

}
