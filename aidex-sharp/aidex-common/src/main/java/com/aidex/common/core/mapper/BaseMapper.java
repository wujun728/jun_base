package com.aidex.common.core.mapper;

import java.util.List;

public interface BaseMapper<T> {
	

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity);
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity);

	/**
	 * 查询数据列表，如果需要分页，请设置分页对象
	 * @param entity
	 * @return
	 */
	public List<T> findListWithUnique(T entity);
	
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	public int insert(T entity);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public int update(T entity);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param entity
	 * @return
	 */
	public int delete(T entity);
	
}