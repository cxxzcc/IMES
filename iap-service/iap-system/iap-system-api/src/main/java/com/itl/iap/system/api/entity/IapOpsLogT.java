package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 操作日志实体类
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@ApiModel(value = "iap_ops_log_t", description = "操作日志实体类")
@Accessors(chain = true)
@TableName("iap_ops_log_t")
public class IapOpsLogT extends BaseModel {
    private static final long serialVersionUID = -43057498470208535L;

    @ApiModelProperty(value = "主键ID")
    @TableId
    private String id;

    @ApiModelProperty(value = "服务器ID")
    @TableField("service_id")
    private String serviceId;

    @ApiModelProperty(value = "服务器名称")
    @TableField("service_name")
    private String serviceName;

    @ApiModelProperty(value = "服务器IP地址")
    @TableField("service_ip")
    private String serviceIp;

    @ApiModelProperty(value = "命名空间")
    @TableField("namespace")
    private String namespace;

    /**
     * 方法类型：(0:接口日志,1:异常日志)
     */
    @ApiModelProperty(value = "方法类型")
    @TableField("method_type")
    private Short methodType;

    @ApiModelProperty(value = "请求方式")
    @TableField("request_function")
    private String requestFunction;

    @ApiModelProperty(value = "请求地址URL")
    @TableField("request_method")
    private String requestMethod;

    @ApiModelProperty(value = "请求代理")
    @TableField("request_proxy")
    private String requestProxy;

    @ApiModelProperty(value = "请求参数")
    @TableField("request_params")
    private String requestParams;

    @ApiModelProperty(value = "日志记录信息")
    @TableField("log_data")
    private String logData;

    @ApiModelProperty(value = "创建用户")
    @TableField("creater")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private Date createDate;

    @ApiModelProperty(value = "创建岗位")
    @TableField("create_org")
    private String createOrg;

    @ApiModelProperty(value = "最后更新人")
    @TableField("last_update_by")
    private String lastUpdateBy;

    @ApiModelProperty(value = "最后更新时间")
    @TableField("last_update_date")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "方法描述信息")
    @TableField("method_desc")
    private String methodDesc;

    /**
     * 执行状态：0 未执行 1已执行 2 执行失败
     */
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    /**
     * 是否成功：0成功 1失败
     */
    @TableField("EXECUTION_FLAG")
    private String executionFlag;

    /**
     * 与之交互的系统
     */
    @TableField("INTERACTIVE_SYSTEM")
    private String interactiveSystem;
}
