package com.ruoyi.client.model;

import com.ruoyi.client.annotation.AVueGroup;

/**
 * 分组相关测试
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/30 - 13:44
 */
public class AVueGroupModel {

    @AVueGroup(prop = "nodeModel", label = "分组测试")
    private AVueNodeModel nodeModel;

    public AVueNodeModel getNodeModel() {
        return nodeModel;
    }

    public void setNodeModel(AVueNodeModel nodeModel) {
        this.nodeModel = nodeModel;
    }
}
