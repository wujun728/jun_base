package com.geek.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 当前在线会话
 * 
 * @author geek
 */
@Schema(title = "当前在线会话")
@Data
public class SysUserOnline {
    /** 会话编号 */
    @Schema(title = "会话编号")
    private String tokenId;

    /** 部门名称 */
    @Schema(title = "部门名称")
    private String deptName;

    /** 用户名称 */
    @Schema(title = "用户名称")
    private String userName;

    /** 登录IP地址 */
    @Schema(title = "登录IP地址")
    private String ipaddr;

    /** 登录地址 */
    @Schema(title = "登录地址")
    private String loginLocation;

    /** 浏览器类型 */
    @Schema(title = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Schema(title = "操作系统")
    private String os;

    /** 登录时间 */
    @Schema(title = "登录时间")
    private Long loginTime;
}
