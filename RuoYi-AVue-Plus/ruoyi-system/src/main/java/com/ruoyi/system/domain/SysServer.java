package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务注册对象 sys_server
 * 
 * @author ruoyi
 * @date 2021-08-27
 */
public class SysServer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 服务名称 */
    @Excel(name = "服务名称")
    private String serverName;

    /** 服务域名路径 */
    @Excel(name = "服务域名路径")
    private String domainPath;

    /** 状态（1正常 -1停用） */
    @Excel(name = "状态", readConverterExp = "1=正常,-=1停用")
    private String status;

    /** 授权token */
    @Excel(name = "授权token")
    private String acceptToken;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setServerName(String serverName) 
    {
        this.serverName = serverName;
    }

    public String getServerName() 
    {
        return serverName;
    }
    public void setDomainPath(String domainPath) 
    {
        this.domainPath = domainPath;
    }

    public String getDomainPath() 
    {
        return domainPath;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setAcceptToken(String acceptToken) 
    {
        this.acceptToken = acceptToken;
    }

    public String getAcceptToken() 
    {
        return acceptToken;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverName", getServerName())
            .append("domainPath", getDomainPath())
            .append("status", getStatus())
            .append("acceptToken", getAcceptToken())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
