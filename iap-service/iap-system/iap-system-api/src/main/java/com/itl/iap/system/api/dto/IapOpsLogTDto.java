package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志dto
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "IapOpsLogTDto", description = "操作日志dto")
public class IapOpsLogTDto implements Serializable {
    private static final long serialVersionUID = -43401375759801708L;
    //分页对象
    private Page page;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 服务器ID
     */
    @ApiModelProperty(value = "服务器ID")
    private String serviceId;
    /**
     * 服务器名称
     */
    @ApiModelProperty(value = "服务器名称")
    private String serviceName;
    /**
     * 服务器IP
     */
    @ApiModelProperty(value = "服务器IP")
    private String serviceIp;
    /**
     * 命名空间
     */
    @ApiModelProperty(value = "命名空间")
    private String namespace;
    /**
     * 方法类型 (0:接口日志,1:异常日志,3:交互接口日志)
     */
    @ApiModelProperty(value = "方法类型 (0:接口日志,1:异常日志,3:交互接口日志)")
    private Short methodType;
    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    private String requestFunction;
    /**
     * 请求方法
     */
    @ApiModelProperty(value = "请求方法")
    private String requestMethod;
    /**
     * 请求代理
     */
    @ApiModelProperty(value = "请求代理")
    private String requestProxy;
    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String requestParams;
    /**
     * 请求日志记录信息
     */
    @ApiModelProperty(value = "请求日志记录信息")
    private String logData;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creater;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 创建岗位
     */
    @ApiModelProperty(value = "创建岗位")
    private String createOrg;
    /**
     * 最后更新人
     */
    @ApiModelProperty(value = "最后更新人")
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 有效期始
     */
    @ApiModelProperty(value = "有效期始")
    private Date createDateStart;
    /**
     * 有效期至
     */
    @ApiModelProperty(value = "有效期至")
    private Date createDateEnd;
    /**
     * 方法描述信息
     */
    @ApiModelProperty(value = "方法描述信息")
    private String methodDesc;

    /**
     * 执行状态：0 未执行 1已执行 2 执行失败
     */
    @ApiModelProperty(value = "执行状态：0 未执行 1已执行 2 执行失败")
    private String executionStatus;

    /**
     * 是否成功：0成功 1失败
     */
    @ApiModelProperty(value = "是否成功：0成功 1失败")
    private String executionFlag;

    /**
     * 与之交互的系统
     */
    @ApiModelProperty(value = "与之交互的系统")
    private String interactiveSystem;
}
