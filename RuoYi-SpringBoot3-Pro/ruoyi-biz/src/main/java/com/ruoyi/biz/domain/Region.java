package com.ruoyi.biz.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 中国行政区划（省市区县乡）对象 biz_region
 *
 * @author 姜彦汐
 * @site https://www.undsky.com
 * @date 2025-09-12
 */
@Data
@TableName("biz_region")
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id, 行政区划代码
     */
    @TableId(type = IdType.AUTO)
    @OrderBy(asc = true, sort = 1)
    private String id;

    /**
     * 上级 行政区划代码
     */
    @Excel(name = "上级 行政区划代码")
    @TableField("parent_id")
    private String parentId;

    /**
     * 名称
     */
    @Excel(name = "名称")
    @TableField("name")
    private String name;

    /**
     * 层级，1=省，2=市，3=区/县，4=乡镇
     */
    @Excel(name = "层级，1=省，2=市，3=区/县，4=乡镇")
    @TableField("level")
    private Integer level;

    /**
     * 首字母大写
     */
    @Excel(name = "首字母大写")
    @TableField("upper_case")
    private String upperCase;

}
