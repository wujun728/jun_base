package com.ruoyi.nocode.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 表单属性对象 my_form_attr
 *
 * @date 2022-08-09
 */
public class MyFormAttr extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 表单id */
    @Excel(name = "表单id")
    private String formId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String attrName;

    /** 属性code */
    @Excel(name = "属性code")
    private String attrCode;

    /** 是否显示：1 是、0 否 */
    @Excel(name = "是否显示：1 是、0 否")
    private Integer isShow;

    /** 排序 */
    @Excel(name = "排序")
    private Long showOrder;

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
    public void setFormId(String formId)
    {
        this.formId = formId;
    }

    public String getFormId()
    {
        return formId;
    }
    public void setAttrName(String attrName)
    {
        this.attrName = attrName;
    }

    public String getAttrName()
    {
        return attrName;
    }
    public void setAttrCode(String attrCode)
    {
        this.attrCode = attrCode;
    }

    public String getAttrCode()
    {
        return attrCode;
    }
    public void setIsShow(Integer isShow)
    {
        this.isShow = isShow;
    }

    public Integer getIsShow()
    {
        return isShow;
    }
    public void setShowOrder(Long showOrder)
    {
        this.showOrder = showOrder;
    }

    public Long getShowOrder()
    {
        return showOrder;
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
            .append("formId", getFormId())
            .append("attrName", getAttrName())
            .append("attrCode", getAttrCode())
            .append("isShow", getIsShow())
            .append("showOrder", getShowOrder())
            .append("createName", getCreateName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
