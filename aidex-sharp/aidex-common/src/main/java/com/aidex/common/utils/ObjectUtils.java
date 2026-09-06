package com.aidex.common.utils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	/**
	 * 转换为 Double 类型
	 */
	public static Double toDouble(final Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return NumberUtils.toDouble(StringUtils.trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为 Float 类型
	 */
	public static Float toFloat(final Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为 Long 类型
	 */
	public static Long toLong(final Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为 Integer 类型
	 */
	public static Integer toInteger(final Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 转换为 Boolean 类型 'true', 'on', 'y', 't', 'yes' or '1'
	 *  (case insensitive) will return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
	}

	/**
	 * 转换为字符串
	 */
	public static String toString(final Object obj) {
		return toString(obj, StringUtils.EMPTY);
	}

	/**
	 * 转换为字符串，如果对象为空，则使用 defaultVal 值
	 */
	public static String toString(final Object obj, final String defaultVal) {
		return obj == null ? defaultVal : obj.toString();
	}

	/**
	 * 转换为字符串，忽略空值。如 null 字符串，则被认为空值，如下：
	 * 		"" to "" ; null to "" ; "null" to "" ; "NULL" to "" ; "Null" to ""
	 */
	public static String toStringIgnoreNull(final Object val) {
		return ObjectUtils.toStringIgnoreNull(val, StringUtils.EMPTY);
	}

	/**
	 * 转换为字符串，忽略空值。如 null 字符串，则被认为空值，如下：
	 * 		"" to defaultVal ; null to defaultVal ; "null" to defaultVal ; "NULL" to defaultVal ; "Null" to defaultVal
	 */
	public static String toStringIgnoreNull(final Object val, String defaultVal) {
		String str = ObjectUtils.toString(val);
		return !"".equals(str) && !"null".equals(str.trim().toLowerCase()) ? str : defaultVal;
	}
}
