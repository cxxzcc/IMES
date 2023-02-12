package com.xxl.job.admin.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/3/19
 */
@Data
@ApiModel(value = "IapXxlJobInfoSaveDto",description = "任务管理保存实体")
public class IapXxlJobInfoSaveDto {


    @ApiModelProperty("id")
    private int id;

    /**
     * 执行器主键ID
     */
    @ApiModelProperty("job_group")
    private int jobGroup;

    /**
     * 任务执行CRON表达式
     */
    @ApiModelProperty("job_cron")
    private String jobCron;

    /**
     * 描述
     */
    @ApiModelProperty("job_desc")
    private String jobDesc;


    /**
     * 负责人
     */
    @ApiModelProperty("author")
    private String author;

    /**
     * 报警邮件
     */
    @ApiModelProperty("alarm_email")
    private String alarmEmail;

    /**
     * 执行器路由策略
     */
    @ApiModelProperty("executor_route_strategy")
    private String executorRouteStrategy;

    /**
     * 执行器，任务Handler名称
     */
    @ApiModelProperty("executor_handler")
    private String executorHandler;

    /**
     * 执行器，任务参数
     */
    @ApiModelProperty("executor_param")
    private String executorParam;

    /**
     * 阻塞处理策略
     */
    @ApiModelProperty("executor_block_strategy")
    private String executorBlockStrategy;

    /**
     * 任务执行超时时间，单位秒
     */
    @ApiModelProperty("executor_timeout")
    private int executorTimeout;

    /**
     * 失败重试次数
     */
    @ApiModelProperty("executor_fail_retry_count")
    private int executorFailRetryCount;

    /**
     * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
     */
    @ApiModelProperty("glue_type")
    private String glueType;

    /**
     * GLUE源代码
     */
    @ApiModelProperty("glue_source")
    private String glueSource;

    /**
     * GLUE备注
     */
    @ApiModelProperty("glue_remark")
    private String glueRemark;

    /**
     * GLUE更新时间
     */
    @ApiModelProperty("glue_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date glueUpdateTime;

    /**
     * 子任务ID，多个逗号分隔
     */
    @ApiModelProperty("child_job_id")
    private String childJobId;

    /**
     * 调度状态：0-停止，1-运行
     */
    @ApiModelProperty("trigger_status")
    private int triggerStatus;

    /**
     * 上次调度时间
     */
    @ApiModelProperty("trigger_last_time")
    private long triggerLastTime;

    /**
     * 下次调度时间
     */
    @ApiModelProperty("trigger_next_time")
    private long triggerNextTime;


}
