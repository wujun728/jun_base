package com.aidex.system.domain;

import com.aidex.common.annotation.Excel;
import com.aidex.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 岗位表 sys_post
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPost extends BaseEntity<SysPost>
{
    private static final long serialVersionUID = 1L;

    /** 岗位编码 */
    @Excel(name = "岗位编码")
    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    private String postCode;

    /** 岗位名称 */
    @Excel(name = "岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    private String postName;

    /** 岗位排序 */
    @Excel(name = "岗位排序")
    @Min(value = 0,message = "最小值必须大于等于0" )
    private int sort;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=删除，2=停用")
    private String status;

    /** 用户是否存在此岗位标识 默认不存在 */
    private boolean flag = false;
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("postCode", getPostCode())
            .append("postName", getPostName())
            .append("postSort", getSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
