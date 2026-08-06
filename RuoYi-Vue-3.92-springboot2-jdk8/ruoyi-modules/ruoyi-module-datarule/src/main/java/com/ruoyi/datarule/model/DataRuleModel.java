package com.ruoyi.datarule.model;

import com.ruoyi.datarule.constant.DataScopeConstant;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataRuleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int ALL = 1; //  全部
    public static final int OWN = 2; //  本人可见
    public static final int OWN_ORG = 3; //  所在机构可见
    public static final int OWN_ORG_CHILDREN = 4; //  所在机构及子级可见
    public static final int CUSTOM = 5; //  自定义

    private String id;

    /**
     * 数据权限字段
     */
    private String scopeColumn = DataScopeConstant.DEFAULT_COLUMN;
    private String userColumn;
    private String userEntityField = DataScopeConstant.DEFAULT_FIELD;

    private String tableName = DataScopeConstant.DEFAULT_TABLE; //数据权限关联表名
    /**
     * 数据权限规则
     */
    private Integer scopeType = ALL;
    /**
     * 可见字段
     */
    private String scopeField;
    /**
     * 数据权限规则值
     */
    private String scopeValue;

    private String scopeClass;

}
