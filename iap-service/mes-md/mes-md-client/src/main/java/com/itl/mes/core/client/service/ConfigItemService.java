package com.itl.mes.core.client.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ConfigItemDto;
import com.itl.mes.core.api.entity.ConfigItem;
import com.itl.mes.core.api.vo.ConfigItemVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ConfigItemServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author chenjx1
 * @date 2021/11/10
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "configItem",
        fallback = ConfigItemServiceImpl.class,
        configuration = FallBackConfig.class)
public interface ConfigItemService {

    /**
     * 保存配置项信息
     * @param configItemVo
     * @return
     * @throws CommonException
     */
    @PostMapping("/configItem/save")
    @ApiOperation(value = "保存配置项信息")
    ResponseData save(@RequestBody ConfigItemVo configItemVo) throws CommonException;

    /**
     * 分页查询配置项信息
     * @param configItemDto
     * @return
     * @throws CommonException
     */
    @PostMapping("/configItem/queryPage")
    @ApiOperation(value = "分页查询配置项信息")
    ResponseData<IPage<ConfigItemDto>> queryPage(@RequestBody ConfigItemDto configItemDto) throws CommonException;

    /**
     * 查询配置项信息
     * @param configItem
     * @return
     * @throws CommonException
     */
    @PostMapping("/configItem/query")
    @ApiOperation(value = "查询配置项信息")
    ResponseData<List<ConfigItem>> query(@RequestBody ConfigItem configItem) throws CommonException;

}
