package com.aidex.common.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseTreeEntity<T> extends BaseEntity<T>
{
    private static final long serialVersionUID = 1L;

    /** 父ID */
    private String parentId;

    /** 所有父ID */
    private String parentIds;

    /** 本级排序号 */
    private Integer treeSort;

    /** 本级排序号和所有父级排序号 */
    private String treeSorts;

    /** 树层级 */
    private Integer treeLevel;

    /** 是否叶子节点 */
    private String treeLeaf;

    /** 子部门 */
    private List<?> children = new ArrayList<>();
}
