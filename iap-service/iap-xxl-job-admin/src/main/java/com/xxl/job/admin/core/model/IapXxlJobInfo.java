package com.xxl.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 调度扩展信息表： 用于保存XXL-JOB调度任务的扩展信息，如任务分组、任务名、机器地址、执行器、执行入参和报警邮件等等
 *
 * @author xuxueli
 * @date 2016-1-12
 * @since jdk1.8
 */
@Accessors(chain = true)
@TableName("xxl_job_info")
@Data
public class IapXxlJobInfo {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 执行器主键ID
     */
    @TableField("job_group")
    private int jobGroup;

    /**
     * 任务执行CRON表达式
     */
    @TableField("job_cron")
    private String jobCron;

    /**
     * 描述
     */
    @TableField("job_desc")
    private String jobDesc;

    /**
     * 添加时间
     */
    @TableField("add_time")
    private Date addTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 负责人
     */
    @TableField("author")
    private String author;

    /**
     * 报警邮件
     */
    @TableField("alarm_email")
    private String alarmEmail;

    /**
     * 执行器路由策略
     */
    @TableField("executor_route_strategy")
    private String executorRouteStrategy;

    /**
     * 执行器，任务Handler名称
     */
    @TableField("executor_handler")
    private String executorHandler;

    /**
     * 执行器，任务参数
     */
    @TableField("executor_param")
    private String executorParam;

    /**
     * 阻塞处理策略
     */
    @TableField("executor_block_strategy")
    private String executorBlockStrategy;

    /**
     * 任务执行超时时间，单位秒
     */
    @TableField("executor_timeout")
    private int executorTimeout;

    /**
     * 失败重试次数
     */
    @TableField("executor_fail_retry_count")
    private int executorFailRetryCount;

    /**
     * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
     */
    @TableField("glue_type")
    private String glueType;

    /**
     * GLUE源代码
     */
    @TableField("glue_source")
    private String glueSource;

    /**
     * GLUE备注
     */
    @TableField("glue_remark")
    private String glueRemark;

    /**
     * GLUE更新时间
     */
    @TableField("glue_update_time")
    private Date glueUpdateTime;

    /**
     * 子任务ID，多个逗号分隔
     */
    @TableField("child_job_id")
    private String childJobId;

    /**
     * 调度状态：0-停止，1-运行
     */
    @TableField("trigger_status")
    private int triggerStatus;

    /**
     * 上次调度时间
     */
    @TableField("trigger_last_time")
    private long triggerLastTime;

    /**
     * 下次调度时间
     */
    @TableField("trigger_next_time")
    private long triggerNextTime;


}
