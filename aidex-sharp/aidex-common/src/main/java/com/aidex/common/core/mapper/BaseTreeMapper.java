package com.aidex.common.core.mapper;

public interface BaseTreeMapper<T> extends BaseMapper<T>{

	/**
	 *  查询当前父节点下，子节点中最大的treeSort值
	 * @param parentId
	 * @return
	 */
	public Integer findMaxTreeSortByParentId(String parentId);


}