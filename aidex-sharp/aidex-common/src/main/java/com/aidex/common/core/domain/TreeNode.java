package com.aidex.common.core.domain;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.aidex.common.core.domain.entity.SysDept;
import com.aidex.common.core.domain.entity.SysMenu;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * 
 * @author ruoyi
 */
@Data
public class TreeNode implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点ID */
    private String key;

    /** 节点名称 */
    private String label;
    /** 节点名称 */
    private String title;

    /** 节点类型：备用 */
    private String type;

    /** 是否可选择 */
    private Boolean selectable;

    /** 是否禁用 */
    private Boolean disabled;

    /** 复选框是否禁用 */
    private Boolean disableCheckbox;

    /** 是否叶子节点 */
    private Boolean isLeaf;

    /** 树层级 */
    private Boolean treeLevel;

    /** 树图标 */
    private String treeIcon;

    /** 父节点ID */
    private String parentIds;

    /** 父节点ID */
    private String parentId;

    /** 子节点 */
    private List<TreeNode> children;

    private HashMap<String,Object> attributes;

    public JSONObject getSlots() {
        return slots;
    }

    public void setSlots(JSONObject slots) {
        this.slots = slots;
    }

    /**
     * 使用 treeNodes 时，可以通过该属性
     */
    private JSONObject slots;
    /**
     * 使用 columns 时，可以通过该属性
     */
    private JSONObject scopedSlots;

    public JSONObject getScopedSlots() {
        return scopedSlots;
    }

    public void setScopedSlots(JSONObject scopedSlots) {
        this.scopedSlots = scopedSlots;
    }

    public TreeNode()
    {

    }

    public TreeNode(SysDept dept)
    {
        //this.id = dept.getDeptId();

        //this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

}
