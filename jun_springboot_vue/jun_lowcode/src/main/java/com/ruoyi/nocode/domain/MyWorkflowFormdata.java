package com.ruoyi.nocode.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 审批记录对象 my_workflow_formdata
 *
 * @date 2022-07-29
 */
public class MyWorkflowFormdata extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 事务key */
    @Excel(name = "事务key")
    private String businessKey;

    /** 任务节点名称 */
    @Excel(name = "任务节点名称")
    private String taskNodeName;

    /** 1同意、2不同意 */
    @Excel(name = "1同意、2不同意")
    private String pass;

    /** 批注 */
    @Excel(name = "批注")
    private String comment;

    /** 创建者名称 */
    @Excel(name = "创建者名称")
    private String createName;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setBusinessKey(String businessKey)
    {
        this.businessKey = businessKey;
    }

    public String getBusinessKey()
    {
        return businessKey;
    }
    public void setTaskNodeName(String taskNodeName)
    {
        this.taskNodeName = taskNodeName;
    }

    public String getTaskNodeName()
    {
        return taskNodeName;
    }
    public void setPass(String pass)
    {
        this.pass = pass;
    }

    public String getPass()
    {
        return pass;
    }
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getComment()
    {
        return comment;
    }
    public void setCreateName(String createName)
    {
        this.createName = createName;
    }

    public String getCreateName()
    {
        return createName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("businessKey", getBusinessKey())
            .append("taskNodeName", getTaskNodeName())
            .append("pass", getPass())
            .append("comment", getComment())
            .append("createName", getCreateName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
