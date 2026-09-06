package com.aidex.system.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import lombok.Data;
import com.aidex.common.core.domain.BaseEntity;

/**
 * 个性化配置对象 sys_table_config
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTableConfig extends BaseEntity<SysTableConfig>
{
    private static final long serialVersionUID = 1L;

    /** 表唯一性编码 */
    @NotBlank(message = "表唯一性编码")
    private String tableKey;

    /** 用户ID */
    private String userId;

    /** 间距 */
    private String space;

    /** 对应列集合JSON */
    private String columns;

    /** 状态（0正常 1删除 2停用） */
    @NotBlank(message = "状态（0正常 1删除 2停用）")
    private String status;

    public void setTableKey(String tableKey)
    {
        this.tableKey = tableKey;
    }

    public String getTableKey()
    {
        return tableKey;
    }

    ;
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

    ;
    public void setSpace(String space)
    {
        this.space = space;
    }

    public String getSpace()
    {
        return space;
    }

    ;
    public void setColumns(String columns)
    {
        this.columns = columns;
    }

    public String getColumns()
    {
        return columns;
    }

    ;
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    ;
    /**
     * 用户区分是个人配置还是系统管理员配置（U个人,S系统配置）
     */
   private String configType;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("tableKey", getTableKey())
                .append("userId", getUserId())
                .append("space", getSpace())
                .append("columns", getColumns())
                .append("remark", getRemark())
                .append("status", getStatus())
                .append("id", getId())
                .append("createBy", getCreateBy())
                .append("createDept", getCreateDept())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("updateIp", getUpdateIp())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .toString();
    }
}