package com.ruoyi.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 系统访问记录表 sys_logininfor
 * 
 * @author ruoyi
 */

@Data
public class SysLogininfor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    @TableId(value = "INFO_ID", type = IdType.AUTO)
    private Long infoId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userName;

    /** 登录状态 0成功 1失败 */
    @Excel(name = "登录状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /** 登录IP地址 */
    @Excel(name = "登录地址")
    private String ipaddr;

    /** 登录地点 */
    @Excel(name = "登录地点")
    private String loginLocation;

    /** 浏览器类型 */
    @Excel(name = "浏览器")
    private String browser;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String os;

    /** 提示消息 */
    @Excel(name = "提示消息")
    private String msg;

    /** 访问时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date loginTime;

    /**
     * 重写 创建者
     */
    @TableField(exist = false)
    private String createBy;

    /**
     * 重写 创建时间
     */
    @TableField(exist = false)
    private Date createTime;

    /**
     * 重写 更新者
     */
    @TableField(exist = false)
    private String updateBy;

    /**
     * 重写 更新时间
     */
    @TableField(exist = false)
    private Date updateTime;

    /**
     * 重写 备注
     */
    @TableField(exist = false)
    private String remark;
   

}
