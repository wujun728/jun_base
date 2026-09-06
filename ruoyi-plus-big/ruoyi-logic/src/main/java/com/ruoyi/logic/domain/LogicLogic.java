package com.ruoyi.logic.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@TableName("logic_logic")
@EqualsAndHashCode(callSuper = true)
@Data
public class LogicLogic extends BaseEntity
{

    @TableId
    private Long id;

    @Excel(name = "备注")
    private String logicRemark;

    @Excel(name = "用户id")
    private Long userId;

    @Excel(name = "部门id")
    private Long deptId;

}
