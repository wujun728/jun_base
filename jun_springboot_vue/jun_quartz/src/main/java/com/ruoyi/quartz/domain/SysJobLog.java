package com.ruoyi.quartz.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 定时任务调度日志表 sys_job_log
 * 
 * @author ruoyi
 */
@Data
public class SysJobLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "日志序号")
    @TableId(value = "JOB_LOG_ID", type = IdType.AUTO)
    private Long jobLogId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** 日志信息 */
    @Excel(name = "日志信息")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String exceptionInfo;

    /** 开始时间 */
    @TableField(exist = false)
    private Date startTime;

    /** 停止时间 */
    @TableField(exist = false)
    private Date stopTime;

    /**
     * 重写 创建者
     */
    @TableField(exist = false)
    private String createBy;

    /**
     * 重写 更新者
     */
    @TableField(exist = false)
    private String updateBy;

    /**
     * 重写 更新时间
     */
    @TableField(exist = false)
    private Date updateTime;

    /**
     * 重写 备注
     */
    @TableField(exist = false)
    private String remark;
}
