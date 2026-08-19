package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 向量库列表对象 vector_database_list
 * 
 * @author 欧阳日峰
 * @date 2025-02-28
 */
public class VectorDatabaseList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 向量数据库列表ID */
    private Long id;

    /** 数据库别名 */
    @Excel(name = "数据库别名")
    private String aliasName;

    /** 数据库地址 */
    @Excel(name = "数据库地址")
    private String host;

    /** 数据库端口 */
    @Excel(name = "数据库端口")
    private Integer port;

    /** 数据库名 */
    @Excel(name = "数据库名")
    private String database;

    /** 数据库表名称 */
    @Excel(name = "数据库表名称")
    private String table;

    /** 数据库用户名 */
    @Excel(name = "数据库用户名")
    private String user;

    /** 数据库密码 */
    @Excel(name = "数据库密码")
    private String password;

    /** 向量纬度 */
    @Excel(name = "向量纬度")
    private Integer dimension;

    /** 是否选择 */
    @Excel(name = "是否选择")
    private Boolean isChose;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setAliasName(String aliasName) 
    {
        this.aliasName = aliasName;
    }

    public String getAliasName() 
    {
        return aliasName;
    }
    public void setHost(String host) 
    {
        this.host = host;
    }

    public String getHost() 
    {
        return host;
    }
    public void setPort(Integer port) 
    {
        this.port = port;
    }

    public Integer getPort() 
    {
        return port;
    }
    public void setDatabase(String database) 
    {
        this.database = database;
    }

    public String getDatabase() 
    {
        return database;
    }
    public void setTable(String table) 
    {
        this.table = table;
    }

    public String getTable() 
    {
        return table;
    }
    public void setUser(String user) 
    {
        this.user = user;
    }

    public String getUser() 
    {
        return user;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setDimension(Integer dimension) 
    {
        this.dimension = dimension;
    }

    public Integer getDimension() 
    {
        return dimension;
    }
    public void setIsChose(Boolean isChose)
    {
        this.isChose = isChose;
    }

    public Boolean getIsChose()
    {
        return isChose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("aliasName", getAliasName())
            .append("host", getHost())
            .append("port", getPort())
            .append("database", getDatabase())
            .append("table", getTable())
            .append("user", getUser())
            .append("password", getPassword())
            .append("dimension", getDimension())
            .append("isChose", getIsChose())
            .toString();
    }
}
