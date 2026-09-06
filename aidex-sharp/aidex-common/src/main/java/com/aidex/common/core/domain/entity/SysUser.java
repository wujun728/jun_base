package com.aidex.common.core.domain.entity;

import com.aidex.common.annotation.Excel;
import com.aidex.common.annotation.Excel.Type;
import com.aidex.common.annotation.Excels;
import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.common.xss.Xss;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity<SysUser> {
    private static final long serialVersionUID = 1L;
    /**
     * 部门ID
     */
    @Excel(name = "部门编号", type = Type.IMPORT)
    private String deptId;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "英文名称")
    private String nameEn;
    @Excel(name = "用户编号")
    private String no;
    /**
     * 用户账号
     */
    @Excel(name = "登录名")
    private String userName;
    /**
     * 用户昵称
     */
    @Excel(name = "别称")
    private String nickName;
    @Excel(name = "用户类型", dictType = "sys_user_type")
    private String userType;
    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    private String email;
    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phonenumber;
    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 密码
     */
    private String password;
    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    /**
     * 民族
     */
    private String nation;
    /**
     * 籍贯
     */
    private String birthAddress;
    /**
     * 政治面貌
     */
    private String polity;
    /**
     * 职称
     */
    private String title;
    /**
     * 办公电话
     */
    private String officeTel;
    /**
     * 传真号
     */
    private String fax;
    /**
     * 工作地点
     */
    private String workSpace;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 工作地点
     */
    private String userPinyin;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;
    /**
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept sysDept;
    /**
     * 角色对象
     */
    private List<SysRole> sysRoles;
    /**
     * 角色组
     */
    private String[] roleIds;
    /**
     * 岗位组
     */
    private String[] postIds;

    public SysUser() {

    }

    private String roleId;
    private String userNameOrName;

    public SysUser(String id) {
        setId(id);
    }

    /**
     * 增加getUserId和setUserId方法为了适配原有代码中主键为user_id
     *
     * @return
     */

    public boolean isAdmin() {
        return isAdmin(getId());
    }

    public static boolean isAdmin(String id) {
        return StringUtils.isNotBlank(id) && "1".equals(id);
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    @Xss(message = "用户账号不能包含脚本字符")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    @Xss(message = "用户账号不能包含脚本字符")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public static void getMenuPermissions(SysUser user) {
        SpringUtils.getBean(SysUserMenu.class).setUserMenu(user);
    }

    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deptId", getDeptId())
                .append("userName", getUserName())
                .append("nickName", getNickName())
                .append("email", getEmail())
                .append("phonenumber", getPhonenumber())
                .append("sex", getSex())
                .append("avatar", getAvatar())
                .append("password", getPassword())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("loginIp", getLoginIp())
                .append("loginDate", getLoginDate())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("dept", getSysDept())
                .toString();
    }
}
