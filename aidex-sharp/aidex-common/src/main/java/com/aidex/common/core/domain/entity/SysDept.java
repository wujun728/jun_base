package com.aidex.common.core.domain.entity;

import com.aidex.common.core.domain.BaseTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 部门表 sys_dept
 * 
 * @author uoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseTreeEntity<SysDept>
{
    private static final long serialVersionUID = 1L;

    /** 部门编号 */
    @NotBlank(message = "部门编码不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    private String deptCode;

    /** 部门名称 */
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    private String deptName;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    private String phone;

    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /** 部门状态:0正常,1停用 */
    private String status;

    @Size(min = 0, max = 200, message = "部门全称长度不能超过200个字符")
    private String deptFullName;
    /**部门类型*/
    private String deptType;
    /**联系地址*/
    private String address;
    /**邮编*/
    private String zipCode;

    private String deptPinyin;

    private String subtitle;
    /**
     * 检索字符串，支持名称，编码，拼音
     */
    private String searchText;

    private String parentName;

    private String parentDeptType;
}
