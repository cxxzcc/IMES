package com.itl.mes.me.api.service;

/**
 * 生产执行判断service
 * @author dengou
 * @date 2021/12/14
 */
public interface ProductionExecutionCheckService {

    /**
     * 生产执行前置判断
     * 1. 判断当前登录用户是否拥有当前工位工序资质证明
     * @return 是否成功， 失败将抛出异常 CommonException
     * */
    Boolean preCheck();


}
