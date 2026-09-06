package com.aidex.common.core.service;

import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service基类
 * @author aidex
 * @email aidex@qq.com
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public interface  BaseService<T> {

	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity);

	/**
	 * 获取单条数据
	 * @return
	 */
	public T get(String id);

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
	public PageInfo<T> findPage(T entity);

	/**
	 * 保存
	 * @param entity
	 * @return boolean
	 */
	@Transactional(readOnly = false)
	public boolean save(T entity);

	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean remove(T entity);

}
