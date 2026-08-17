package com.ruoyi.datarule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_data_rule")
public class SysDataRule {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("apply_all_role")
    private String applyAllRole;

    @TableField("role_ids")
    private String roleIds;

    @TableField("model_name")
    private String modelName;

    private String rules;

    private String enable;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_time")
    private Date updateTime;

    private String remark;
}