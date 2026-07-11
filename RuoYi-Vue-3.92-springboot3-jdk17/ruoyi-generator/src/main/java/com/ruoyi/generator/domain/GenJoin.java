package com.ruoyi.generator.domain;

import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GenJoin extends BaseEntity {

    /** 表编号 */
    private Long tableId;

    /** 关联左表编号 */
    private Long leftTableId;

    /** 关联右表编号 */
    private Long rightTableId;

    /** 关联关系中新引入的表 */
    private Long newTableId;

    /** 关联左表别名 */
    private String leftTableAlias;

    /** 关联右表别名 */
    private String rightTableAlias;

    /** 关联左表外键 */
    private Long leftTableFk;

    /** 关联右表外键 */
    private Long rightTableFk;

    /** 关联类型 */
    private String joinType;

    /** 关联字段 */
    private List<String> joinColumns;

    /** 关联顺序 */
    private Long orderNum;
}
