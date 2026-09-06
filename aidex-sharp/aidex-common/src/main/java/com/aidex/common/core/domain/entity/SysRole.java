package com.aidex.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.aidex.common.annotation.Excel;
import com.aidex.common.annotation.Excel.ColumnType;
import com.aidex.common.core.domain.BaseEntity;

/**
 * 角色表 sys_role
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity<SysRole>
{
    private static final long serialVersionUID = 1L;


    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 角色权限 */
    @Excel(name = "角色权限")
    private String roleKey;

    /** 角色排序 */
    @Excel(name = "角色排序")
    private String sort;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限） */
    @Excel(name = "数据范围", readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String dataScope;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    private boolean deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户是否存在此角色标识 默认不存在 */
    private boolean flag = false;

    /** 菜单组 */
    private String[] menuIds;

    /** 部门组（数据权限） */
    private String[] deptIds;

    public SysRole()
    {

    }

    public SysRole(String id)
    {
        setId(id);
    }



    public boolean isAdmin()
    {
        return isAdmin(this.getId());
    }

    public static boolean isAdmin(String id)
    {
        return  StringUtils.isNotBlank(id) && "1".equals(id);
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotBlank(message = "显示顺序不能为空")
    public String getSort()
    {
        return sort;
    }

    public void setRoleSort(String sort)
    {
        this.sort = sort;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
    }

    public boolean isMenuCheckStrictly()
    {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly)
    {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly()
    {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly)
    {
        this.deptCheckStrictly = deptCheckStrictly;
    }

   private String codeOrName;

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    private String option;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("sort", getSort())
            .append("dataScope", getDataScope())
            .append("menuCheckStrictly", isMenuCheckStrictly())
            .append("deptCheckStrictly", isDeptCheckStrictly())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("updateIp", getUpdateIp())
            .append("version", getVersion())
            .append("remark", getRemark())
            .toString();
    }
}
