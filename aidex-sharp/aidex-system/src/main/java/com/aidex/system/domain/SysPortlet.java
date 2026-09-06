package com.aidex.system.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import lombok.Data;
import com.aidex.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.aidex.common.annotation.Excel;
/**
 * 门户小页管理对象 sys_portlet
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPortlet extends BaseEntity<SysPortlet>
{
    private static final long serialVersionUID = 1L;

    /** 小页名称 */
    @Excel(name = "小页名称")
    @NotBlank(message = "小页名称不允许为空")
    private String name;

    /** 小页编码 */
    @Excel(name = "小页编码")
    @NotBlank(message = "小页编码不允许为空")
    private String code;

    /** 小页ULRL */
    @Excel(name = "小页ULRL")
    private String url;

    /** 刷新频率 */
    @Excel(name = "刷新频率")
    private String refreshRate;

    /** 是否显示标题 */
    @Excel(name = "是否显示标题", dictType = "sys_yes_no")
    private String showTitle;

    /** 是否允许拖拽 */
    @Excel(name = "是否允许拖拽", dictType = "sys_yes_no")
    private String isAllowDrag;

    /** 横向栅格数 */
    @Excel(name = "横向栅格数")
    private String xGridNumber;

    /** 纵向栅格数 */
    @Excel(name = "纵向栅格数")
    private String yGridNumber;

    /** 排序号 */
    @Excel(name = "排序号")
    private Long sort;

    /** 状态 */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    @NotBlank(message = "状态不允许为空")
    private String status;

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setRefreshRate(String refreshRate) 
    {
        this.refreshRate = refreshRate;
    }

    public String getRefreshRate() 
    {
        return refreshRate;
    }

    public void setShowTitle(String showTitle) 
    {
        this.showTitle = showTitle;
    }

    public String getShowTitle() 
    {
        return showTitle;
    }

    public void setIsAllowDrag(String isAllowDrag) 
    {
        this.isAllowDrag = isAllowDrag;
    }

    public String getIsAllowDrag() 
    {
        return isAllowDrag;
    }

    public void setxGridNumber(String xGridNumber) 
    {
        this.xGridNumber = xGridNumber;
    }

    public String getxGridNumber() 
    {
        return xGridNumber;
    }

    public void setyGridNumber(String yGridNumber) 
    {
        this.yGridNumber = yGridNumber;
    }

    public String getyGridNumber() 
    {
        return yGridNumber;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

   private String roleId;
    private String userId;//权限过滤时使用
    private String menuId;//门户小页对应的权限表菜单ID
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("name", getName())
            .append("code", getCode())
            .append("url", getUrl())
            .append("refreshRate", getRefreshRate())
            .append("showTitle", getShowTitle())
            .append("isAllowDrag", getIsAllowDrag())
            .append("xGridNumber", getxGridNumber())
            .append("yGridNumber", getyGridNumber())
            .append("sort", getSort())
            .append("status", getStatus())
            .append("remark", getRemark())
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
