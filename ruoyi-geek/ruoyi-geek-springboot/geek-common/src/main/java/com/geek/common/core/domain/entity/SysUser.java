package com.geek.common.core.domain.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Excel.ColumnType;
import com.geek.common.annotation.Excel.Type;
import com.geek.common.annotation.Excels;
import com.geek.common.annotation.Xss;
import com.geek.common.core.domain.BaseEntity;
import com.geek.common.utils.SecurityUtils;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.RelationManyToOne;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户对象 sys_user
 * 
 * @author geek
 */
@Table("sys_user")
@Schema(title = "用户")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Id
    @Schema(title = "用户序号")
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /** 部门ID */
    @Schema(title = "部门编号")
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    /** 用户账号 */
    @Schema(title = "登录名称")
    @Excel(name = "登录名称")
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String userName;

    /** 用户昵称 */
    @Schema(title = "用户名称")
    @Excel(name = "用户名称")
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    /** 用户邮箱 */
    @Schema(title = "用户邮箱")
    @Excel(name = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /** 手机号码 */
    @Schema(title = "手机号码")
    @Excel(name = "手机号码")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String phonenumber;

    /** 用户性别 */
    @Schema(title = "用户性别", description = "0=男,1=女,2=未知")
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 用户头像 */
    @Schema(title = "用户头像")
    private String avatar;

    /** 密码 */
    @Schema(title = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /** 账号状态（0正常 1停用） */
    @Schema(title = "账号状态", description = "0正常 1停用")
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Schema(title = "删除标志", description = "0代表存在 2代表删除")
    private Integer delFlag;

    /** 最后登录IP */
    @Schema(title = "最后登录IP")
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /** 最后登录时间 */
    @Schema(title = "最后登录时间")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /** 密码最后更新时间 */
    @Schema(title = "密码最后更新时间")
    @Excel(name = "密码最后更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date pwdUpdateDate;

    /** 部门对象 */
    @Schema(title = "部门对象")
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    @RelationManyToOne(selfField = "deptId", targetField = "deptId")
    private SysDept dept;

    /** 角色对象 */
    @Schema(title = "角色对象")
    @RelationManyToMany(joinTable = "sys_user_role", selfField = "userId", joinSelfColumn = "user_id", targetField = "roleId", joinTargetColumn = "role_id")
    private List<SysRole> roles;

    /** 角色组 */
    @Schema(title = "角色组")
    @RelationManyToMany(joinTable = "sys_user_role", selfField = "userId", joinSelfColumn = "user_id", targetField = "roleId", joinTargetColumn = "role_id", targetTable = "sys_role", valueField = "roleId")
    private List<Long> roleIds;

    /** 岗位组 */
    @Schema(title = "岗位组")
    @RelationManyToMany(joinTable = "sys_user_post", selfField = "userId", joinSelfColumn = "user_id", targetField = "postId", joinTargetColumn = "post_id", targetTable = "sys_post", valueField = "postId")
    private List<Long> postIds;

    /** 角色ID */
    @Schema(title = "角色ID")
    @Column(ignore = true)
    private Long roleId;

    public boolean isAdmin() {
        return SecurityUtils.isAdmin(this.userId);
    }

    public SysUser() {

    }

    public SysUser(Long userId) {
        this.userId = userId;
    }
}
