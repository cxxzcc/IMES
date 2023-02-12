package com.itl.iap.common.base.utils;

import com.itl.iap.common.base.aop.JdbcConfig;
import com.itl.iap.common.base.dto.IapOpsLogTDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @create 2021/12/30
 * @description 接口日志工具类
 **/
@Log4j2
@Component
public class LogUtil {

    @Resource
    JdbcConfig jdbcConfig;

    /**
     * @param type            日志类型
     * @param params          请求参数
     * @param requestMethod   请求地址
     * @param logMessage      记录信息
     * @param executionStatus 执行状态
     * @param methodDesc      接口描述
     * @param executionFlag   是否成功
     */
    public static void log(String type, String params, String requestMethod, String logMessage, String executionStatus, String methodDesc, String executionFlag, JdbcConfig jdbcConfig) {
        IapOpsLogTDto logDto = new IapOpsLogTDto();
        //记录日志表
        Date date = new Date();
        // 记录日志
        logDto.setId(uuid32()).setCreateDate(date).setLastUpdateDate(date);
        // 访问IP地址
        logDto.setServiceIp("");
        // 请求路径url(方法类型：(0:接口日志,1:异常日志,3:交互日志))
        logDto.setMethodType((short) 3);
        // 请求方法
        logDto.setRequestMethod(requestMethod);
        // 服务名称
        logDto.setRequestFunction(type);
        // 用户使用的浏览器
        logDto.setServiceName("");
        // nacos命名空间
        logDto.setRequestProxy("");
        // 服务id
        logDto.setNamespace("");
        logDto.setServiceId("");
        // 请求参数
        logDto.setRequestParams(params);
        // 记录信息
        logDto.setLogData(logMessage);
        // 执行状态
        logDto.setExecutionStatus(executionStatus);
        // 接口描述
        logDto.setMethodDesc(methodDesc);
        // 是否成功
        logDto.setExecutionFlag(executionFlag);
        // 与之交互的系统
        logDto.setInteractiveSystem("");
        try {
            jdbcConfig.insertSystemLog(logDto);
        } catch (Exception e) {
            log.error("===========================>日志存储错误:" + e.getMessage());
        }
    }

    /**
     * 需要保存的日志对象
     * @param iapOpsLogTDto
     */
    public static void log(IapOpsLogTDto iapOpsLogTDto,JdbcConfig jdbcConfig) {
        IapOpsLogTDto logDto = new IapOpsLogTDto();
        //记录日志表
        Date date = new Date();
        // 记录日志
        logDto.setId(uuid32()).setCreateDate(date).setLastUpdateDate(date);
        // 访问IP地址
        String serviceIp = iapOpsLogTDto.getServiceIp();
        logDto.setServiceIp(serviceIp);
        // 方法类型(方法类型：(0:接口日志,1:异常日志,3:交互日志))（必填）
        short methodType = iapOpsLogTDto.getMethodType();
        logDto.setMethodType(methodType);
        // 请求方法（必填）
        String requestMethod = iapOpsLogTDto.getRequestMethod();
        logDto.setRequestMethod(requestMethod);
        // 请求方式
        String requestFunction = iapOpsLogTDto.getRequestFunction();
        logDto.setRequestFunction(requestFunction);
        // 服务器名称
        String serviceName = iapOpsLogTDto.getServiceName();
        logDto.setServiceName(serviceName);
        // 请求代理
        String requestProxy = iapOpsLogTDto.getRequestProxy();
        logDto.setRequestProxy(requestProxy);
        // 服务器ID
        String serviceId = iapOpsLogTDto.getServiceId();
        logDto.setServiceId(serviceId);
        // 命名空间
        String namespace = iapOpsLogTDto.getNamespace();
        logDto.setNamespace(namespace);
        // 请求参数（必填）
        String requestParams = iapOpsLogTDto.getRequestParams();
        logDto.setRequestParams(requestParams);
        // 记录信息（必填）
        String logData = iapOpsLogTDto.getLogData();
        logDto.setLogData(logData);
        // 执行状态（必填）
        String executionStatus = iapOpsLogTDto.getExecutionStatus();
        logDto.setExecutionStatus(executionStatus);
        // 接口描述
        String methodDesc = iapOpsLogTDto.getMethodDesc();
        logDto.setMethodDesc(methodDesc);
        // 是否成功（必填）
        String executionFlag = iapOpsLogTDto.getExecutionFlag();
        logDto.setExecutionFlag(executionFlag);
        // 与之交互的系统
        String interactiveSystem = iapOpsLogTDto.getInteractiveSystem();
        logDto.setInteractiveSystem(interactiveSystem);
        try {
            jdbcConfig.insertSystemLog(logDto);
        } catch (Exception e) {
            log.error("===========================>日志存储错误:" + e.getMessage());
        }
    }


    /**
     * 生成 UUID
     */
    public static String uuid32() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

}
