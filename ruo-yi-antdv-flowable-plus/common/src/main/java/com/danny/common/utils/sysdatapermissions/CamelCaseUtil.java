package com.danny.common.utils.sysdatapermissions;

/**
 * 数据库字段转换成驼峰工具类
 */
public class CamelCaseUtil {

	/**
	 * 将数据库字段转换成驼峰命名
	 * @param name
	 * @return
	 */
	public static String toCamelCase(CharSequence name) {
		if (null == name) {
			return null;
		}
		String camelName = name.toString();
		if (camelName.contains("_")) {
			StringBuilder sb = new StringBuilder(camelName.length());
			boolean upperCase = false;
			for (int i = 0; i < camelName.length(); i++) {
				char c = camelName.charAt(i);
				if (c == '_') {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
			}
			return sb.toString();
		}
		return camelName;
	}

}
