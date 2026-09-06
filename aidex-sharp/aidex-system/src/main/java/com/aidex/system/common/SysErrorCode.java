package com.aidex.system.common;

import com.aidex.common.exception.ErrorCodeI;

/**
 * There are only 3 basic ErrorCode:
 * COLA_ERROR
 * BIZ_ERROR
 * SYS_ERROR
 * <p>
 * Created by fulan.zjf on 2017/12/18.
 */
public enum SysErrorCode implements ErrorCodeI {

    //岗位信息
    B_SYSPOST_PostNameAlreadyExist("B_SYSPOST_PostNameAlreadyExist", "岗位名称已存在"),

    B_SYSPOST_PostCodeAlreadyExist("B_SYSPOST_PostCodeAlreadyExist", "岗位编号已存在"),

    B_SYSDEPT_DeptNameAlreadyExist("B_SYSDEPT_DeptNameAlreadyExist", "部门名称已存在"),

    B_SYSDEPT_DeptIdNotEqualParentId("B_SYSDEPT_DeptIdNotEqualParentId", "修改部门失败，上级部门不能是自己"),

    B_SYSDEPT_DeptAlreadyUnNormal("B_SYSDEPT_DeptAlreadyUnNormal", "部门停用，不允许新增子部门"),

    B_SYSDEPT_DeptHasNormalChild("B_SYSDEPT_DeptHasNormalChild", "该部门包含未停用的子部门，不允许标记为停用"),

    B_SYSDEPT_NoAllowUpdateType("B_SYSDEPT_NoAllowUpdateType", "节点下存在公司节点，不允许修改"),

    B_SYSDEPT_DeptHasChildNotDelete("B_SYSDEPT_DeptHasChildNotDelete", "该部门包含子部门，不允许删除"),

    B_SYSDEPT_DeptHasUserNotDelete("B_SYSDEPT_DeptHasUserNotDelete", "该部门包含用户，不允许删除"),

    B_SYSDEPT_DeptMoveUnNormal("B_SYSDEPT_DeptMoveUnNormal", "非法移动，不允许将节点移动到自身或者子节点下"),

    B_SYSDICTTYPE_DictTypeAlreadyExist("B_SYSDICTTYPE_DictTypeAlreadyExist", "数据字典类型已存在"),

    B_SYSDICDATA_DictValueAlreadyExist("B_SYSDICDATA_DictValueAlreadyExist", "数据键值已存在"),

    B_SYSROLE_RoleNameAlreadyExist("B_SYSROLE_RoleNameAlreadyExist", "角色名称已存在"),

    B_SYSROLE_RoleKeyAlreadyExist("B_SYSROLE_RoleKeyAlreadyExist", "角色编号已存在"),

    B_SYSCONFIG_ConfigKeyAlreadyExist("B_SYSCONFIG_ConfigKeyAlreadyExist", "参数键值已存在"),


    B_SYSUSER_UserNameAlreadyExist("B_SYSUSER_UserNameAlreadyExist", "用户名称已存在"),

    B_SYSUSER_PhoneAlreadyExist("B_SYSUSER_PhoneAlreadyExist", "用户手机号已存在"),

    B_SYSUSER_EmailAlreadyExist("B_SYSUSER_EmailAlreadyExist", "用户邮箱已存在"),

    B_SYSMENU_MenuHasChildNotDelete("B_SYSMENU_MenuHasChildNotDelete", "该菜单包含子菜单，不允许删除"),

    B_SYSMENU_MenuAlreadyUnNormal("B_SYSMENU_MenuAlreadyUnNormal", "菜单停用，不允许新增子菜单"),

    B_SYSMENU_MenuMoveUnNormal("B_SYSMENU_MenuMoveUnNormal", "非法移动，不允许将节点移动到自身或者子节点下"),

    B_SYSMENU_MenuIdNotEqualParentId("B_SYSMENU_MenuIdNotEqualParentId", "修改菜单失败，父级菜单不能是自己"),

    B_SYSMENU_MenuHasNormalChild("B_SYSMENU_MenuHasNormalChild", "该菜单包含未停用的子菜单，不允许标记为停用"),

    B_SYSMENU_MenuNameAlreadyExist("B_SYSMENU_MenuNameAlreadyExist", "菜单名称已存在"),

    B_SYSMENU_PathAlreadyExist("B_SYSMENU_PathAlreadyExist", "路由地址已存在"),

    B_SYSMENU_PathStartsWithHttp("B_SYSMENU_PathStartsWithHttp", "路由地址必须以http(s)://开头"),


    B_FILEUPLOADER_UploaderErr("B_FILEUPLOADER_UploaderErr", "上传失败"),

    B_FILEUPLOADER_FileNotExist("B_FILEUPLOADER_FileNotExist", "附件不存在"),

    B_FILEUPLOADER_DownloadFileNotExist("B_FILEUPLOADER_DownloadFileNotExist", "下载文件已不存在"),

    B_FILEUPLOADER_NoFindFileServer("B_FILEUPLOADER_NoFindFileServer", "没有找到附件处理器"),

    BIZ_ERROR("BIZ_ERROR", "通用的业务逻辑错误"),

    COLA_ERROR("COLA_FRAMEWORK_ERROR", "COLA框架错误"),

    SYS_ERROR("SYS_ERROR", "未知的系统错误");

    private String errCode;
    private String errDesc;

    private SysErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getErrDesc() {
        return errDesc;
    }
}
