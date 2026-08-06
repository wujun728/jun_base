package com.ruoyi.datarule.model;

import com.ruoyi.common.core.domain.TreeEntity;
import lombok.Data;

/**
 * 树抽象实体基类
 */
@Data
public class TreeEntityModel extends TreeEntity {
    protected String id; // 编号
    protected String name; // 资源名称

}
