package com.ruoyi.nocode.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 表单定义对象 my_form_def
 *
 * @date 2022-07-27
 */
public class MyFormDef extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 表单名称 */
    @Excel(name = "表单名称")
    private String name;

    /** 表单定义 */
    @Excel(name = "表单定义")
    private String defination;

    /** $column.columnComment */
    @Excel(name = "表单定义")
    private String refProcKey;

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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setDefination(String defination)
    {
        this.defination = defination;
    }

    public String getDefination()
    {
        return defination;
    }
    public void setRefProcKey(String refProcKey)
    {
        this.refProcKey = refProcKey;
    }

    public String getRefProcKey()
    {
        return refProcKey;
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
            .append("name", getName())
            .append("defination", getDefination())
            .append("refProcKey", getRefProcKey())
            .append("createName", getCreateName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
