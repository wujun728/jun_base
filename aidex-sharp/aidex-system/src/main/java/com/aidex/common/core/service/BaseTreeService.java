package com.aidex.common.core.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BaseTreeService<T> extends BaseService<T> {

    /**
     * 根据父ID查询子节点的
     *
     * @param parentId 父ID
     * @return
     */
    public List<T> findChildListByParentId(String parentId);

    /**
     * 根据树路径查询所有的子节点信息
     * @param parentIds
     * @return
     */
    public List<T> findChildListByParentIds(String parentIds);

    /**
     * 查询最大编号
     * @param domain
     * @return
     */
    public Integer findMaxSort(T domain);

}
