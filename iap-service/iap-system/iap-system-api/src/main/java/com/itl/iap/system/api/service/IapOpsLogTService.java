package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;

import java.util.List;

/**
 * 操作日志service
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface IapOpsLogTService extends IService<IapOpsLogT>{

    /**
     * 分页查询全部
     *
     * @param
     * @return
     */
    IPage<IapOpsLogTDto> pageQuery(IapOpsLogTDto iapOpsLogDto);

    /**
     * 系统接口日志，分页查询
     * methodType = (short) 0
     * @param
     * @return
     */
    IPage<IapOpsLogTDto> pageQueryTypeInterface(IapOpsLogTDto iapOpsLogDto);

    /**
     * 异常日志，分页查询
     * methodType = (short) 1
     * @param
     * @return
     */
    IPage<IapOpsLogTDto> pageQueryException(IapOpsLogTDto iapOpsLogDto);

    /**
     * 交互接口日志，分页查询
     *  methodType = (short) 3
     * @param
     * @return
     */
    IPage<IapOpsLogTDto> pageQueryInteractive(IapOpsLogTDto iapOpsLogDto);

    /**
     * 交互接口日志
     *  methodType = (short) 3
     * @param
     * @return
     */
    List<IapOpsLogT> listQueryInteractive(IapOpsLogT iapOpsLog);


    List<IapOpsLogT> getByIds(List<String> ids);

    /**
     * 交互接口推送
     * @param ids
     * @return
     */
    ResponseData call(List<String> ids);

}
