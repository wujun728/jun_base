package com.xiaoleilu.hutool.clone;

/**
 * 克隆支持类，提供默认的克隆方法
 * @author Wujun
 *
 * @param <T>
 */
public class CloneSupport<T> implements Cloneable<T>{
	
	@SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}