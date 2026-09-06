package com.ruoyi.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;

@Component
public class MybatisUtils {
    private final SqlSessionFactory sqlSessionFactory;

    @Autowired
    public MybatisUtils(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * 提取中括号内的字符串
     * 例如：params[endcreateTime] -> endcreateTime
     *
     * @param input 输入字符串
     * @return 中括号内的字符串，如果没有找到则返回null
     */
    private String extractBracketContent(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }

        int startIndex = input.indexOf('[');
        int endIndex = input.indexOf(']');

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return input.substring(startIndex + 1, endIndex);
        }

        return null;
    }

    /**
     * 根据实体类字段类型转换字符串值
     * 解决 PostgreSQL 中 bigint = character varying / date = character varying 类型不匹配问题
     *
     * @param type      实体类类型
     * @param fieldName 字段名（驼峰命名）
     * @param value     字符串值
     * @return 转换后的值
     */
    private <T> Object convertValue(Class<T> type, String fieldName, String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        try {
            // 将下划线命名转换为驼峰命名以匹配实体类字段
            String camelFieldName = toCamelCase(fieldName);
            Field field = getFieldFromClass(type, camelFieldName);
            if (field == null) {
                // 尝试直接使用原字段名
                field = getFieldFromClass(type, fieldName);
            }
            if (field != null) {
                Class<?> fieldType = field.getType();
                if (fieldType == Long.class || fieldType == long.class) {
                    return Long.parseLong(value);
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    return Integer.parseInt(value);
                } else if (fieldType == Double.class || fieldType == double.class) {
                    return Double.parseDouble(value);
                } else if (fieldType == Float.class || fieldType == float.class) {
                    return Float.parseFloat(value);
                } else if (fieldType == BigDecimal.class) {
                    return new BigDecimal(value);
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    return Boolean.parseBoolean(value);
                } else if (fieldType == Short.class || fieldType == short.class) {
                    return Short.parseShort(value);
                } else if (fieldType == Byte.class || fieldType == byte.class) {
                    return Byte.parseByte(value);
                } else if (fieldType == LocalDate.class) {
                    return parseLocalDate(value);
                } else if (fieldType == LocalDateTime.class) {
                    return parseLocalDateTime(value);
                } else if (fieldType == Date.class) {
                    return parseDate(value);
                }
            }
        } catch (NumberFormatException e) {
            // 转换失败，返回原字符串值
        }
        return value;
    }

    /**
     * 解析 LocalDate，支持多种格式
     */
    private LocalDate parseLocalDate(String value) {
        String[] patterns = { "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd" };
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignored) {
            }
        }
        return LocalDate.parse(value);
    }

    /**
     * 解析 LocalDateTime，支持多种格式
     */
    private LocalDateTime parseLocalDateTime(String value) {
        String[] patterns = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd" };
        for (String pattern : patterns) {
            try {
                if (pattern.equals("yyyy-MM-dd") && value.length() == 10) {
                    return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern)).atStartOfDay();
                }
                return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignored) {
            }
        }
        return LocalDateTime.parse(value);
    }

    /**
     * 解析 Date，支持多种格式
     */
    private Date parseDate(String value) {
        String[] patterns = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd" };
        for (String pattern : patterns) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
                return sdf.parse(value);
            } catch (java.text.ParseException ignored) {
            }
        }
        return null;
    }

    /**
     * 从类及其父类中获取字段
     */
    private Field getFieldFromClass(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 将下划线命名转换为驼峰命名
     */
    private String toCamelCase(String underscoreName) {
        if (StringUtils.isEmpty(underscoreName)) {
            return underscoreName;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < underscoreName.length(); i++) {
            char c = underscoreName.charAt(i);
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    /**
     * 检查参数键是否在忽略字段列表中
     *
     * @param paramKey     请求参数的键名
     * @param ignoreFields 忽略的字段列表
     * @return 如果应该忽略则返回 true
     */
    private boolean isIgnoredField(String paramKey, String[] ignoreFields) {
        if (ignoreFields == null || ignoreFields.length == 0) {
            return false;
        }
        for (String ignoreField : ignoreFields) {
            if (StringUtils.isEmpty(ignoreField)) {
                continue;
            }
            // 完全匹配
            if (StringUtils.equals(paramKey, ignoreField)) {
                return true;
            }
            // 带后缀的匹配 (如 xxxEq, xxxLike 等)
            if (StringUtils.startsWith(paramKey, ignoreField)) {
                String suffix = paramKey.substring(ignoreField.length());
                if (suffix.isEmpty() || Character.isUpperCase(suffix.charAt(0))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据请求参数动态构建 MyBatis-Plus 的 QueryWrapper 查询条件
     * 支持的条件方法有：
     * - xxxAsc：升序排序
     * - xxxDesc：降序排序
     * - xxxIsNull：字段为null
     * - xxxEq：等于
     * - xxxNe：不等于
     * - xxxGt：大于
     * - xxxGe：大于等于
     * - xxxLt：小于
     * - xxxLe：小于等于
     * - xxxBetween：区间
     * - xxxNotBetween：不在区间
     * - xxxLike：模糊匹配
     * - xxxNotLike：不模糊匹配
     * - xxxLikeRight：右模糊
     * - xxxLikeLeft：左模糊
     * - xxxNotLikeRight：右不模糊
     * - xxxNotLikeLeft：左不模糊
     * - xxxInSql：IN 查询
     * - xxxNotInSql：NOT IN 查询
     */
    public <T> QueryWrapper<T> getQueryWrapper(Class<T> type) {
        return getQueryWrapper(type, new String[0]);
    }

    /**
     * 根据请求参数动态构建 MyBatis-Plus 的 QueryWrapper 查询条件
     *
     * @param type         实体类类型
     * @param ignoreFields 忽略的字段列表，这些字段不会被添加到查询条件中
     * @return QueryWrapper
     */
    public <T> QueryWrapper<T> getQueryWrapper(Class<T> type, String... ignoreFields) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Map<String, String> reqParams = ServletUtils.getParamMap(ServletUtils.getRequest());
        if (reqParams != null && reqParams.size() > 0) {
            for (Map.Entry<String, String> entry : reqParams.entrySet()) {
                if (StringUtils.equals("pageSize", entry.getKey()) || StringUtils.equals("pageNum", entry.getKey())
                        || StringUtils.equals("action", entry.getKey())) {
                    continue;
                }
                // 检查是否在忽略列表中
                if (isIgnoredField(entry.getKey(), ignoreFields)) {
                    continue;
                }
                if (StringUtils.startsWith(entry.getKey(), "params[")) {
                    String key = extractBracketContent(entry.getKey());
                    if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(entry.getValue())) {
                        if (StringUtils.startsWith(key, "begin")) {
                            String fieldName = key.substring(5);
                            queryWrapper.ge(fieldName, convertValue(type, fieldName, entry.getValue()));
                        } else if (StringUtils.startsWith(key, "end")) {
                            String fieldName = key.substring(3);
                            queryWrapper.le(fieldName, convertValue(type, fieldName, entry.getValue()));
                        } else {
                            queryWrapper.eq(key, convertValue(type, key, entry.getValue()));
                        }
                    }
                    continue;
                }
                if (StringUtils.endsWith(entry.getKey(), "Asc")) {
                    queryWrapper.orderByAsc(entry.getKey().substring(0, entry.getKey().length() - 3));
                    continue;
                }
                if (StringUtils.endsWith(entry.getKey(), "Desc")) {
                    queryWrapper.orderByDesc(entry.getKey().substring(0, entry.getKey().length() - 4));
                    continue;
                }
                if (StringUtils.endsWith(entry.getKey(), "IsNull")) {
                    queryWrapper.isNull(entry.getKey().substring(0, entry.getKey().length() - 6));
                    continue;
                }
                if (StringUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                if (StringUtils.endsWith(entry.getKey(), "Eq")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 2);
                    queryWrapper.eq(fieldName, convertValue(type, fieldName, entry.getValue()));
                } else if (StringUtils.endsWith(entry.getKey(), "Ne")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 2);
                    queryWrapper.ne(fieldName, convertValue(type, fieldName, entry.getValue()));
                } else if (StringUtils.endsWith(entry.getKey(), "Gt")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 2);
                    queryWrapper.gt(fieldName, convertValue(type, fieldName, entry.getValue()));
                } else if (StringUtils.endsWith(entry.getKey(), "Ge")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 2);
                    queryWrapper.ge(fieldName, convertValue(type, fieldName, entry.getValue()));
                } else if (StringUtils.endsWith(entry.getKey(), "Lt")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 2);
                    queryWrapper.lt(fieldName, convertValue(type, fieldName, entry.getValue()));
                } else if (StringUtils.endsWith(entry.getKey(), "Le")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 2);
                    queryWrapper.le(fieldName, convertValue(type, fieldName, entry.getValue()));
                } else if (StringUtils.endsWith(entry.getKey(), "Between")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 7);
                    String[] values = entry.getValue().split(",");
                    queryWrapper.between(fieldName, convertValue(type, fieldName, values[0]),
                            convertValue(type, fieldName, values[1]));
                } else if (StringUtils.endsWith(entry.getKey(), "NotBetween")) {
                    String fieldName = entry.getKey().substring(0, entry.getKey().length() - 10);
                    String[] values = entry.getValue().split(",");
                    queryWrapper.notBetween(fieldName, convertValue(type, fieldName, values[0]),
                            convertValue(type, fieldName, values[1]));
                } else if (StringUtils.endsWith(entry.getKey(), "Like")) {
                    queryWrapper.like(entry.getKey().substring(0, entry.getKey().length() - 4), entry.getValue());
                } else if (StringUtils.endsWith(entry.getKey(), "NotLike")) {
                    queryWrapper.notLike(entry.getKey().substring(0, entry.getKey().length() - 7), entry.getValue());
                } else if (StringUtils.endsWith(entry.getKey(), "LikeRight")) {
                    queryWrapper.likeRight(entry.getKey().substring(0, entry.getKey().length() - 9), entry.getValue());
                } else if (StringUtils.endsWith(entry.getKey(), "LikeLeft")) {
                    queryWrapper.likeLeft(entry.getKey().substring(0, entry.getKey().length() - 8), entry.getValue());
                } else if (StringUtils.endsWith(entry.getKey(), "NotLikeRight")) {
                    queryWrapper.notLikeRight(entry.getKey().substring(0, entry.getKey().length() - 12),
                            entry.getValue());
                } else if (StringUtils.endsWith(entry.getKey(), "NotLikeLeft")) {
                    queryWrapper.notLikeLeft(entry.getKey().substring(0, entry.getKey().length() - 11),
                            entry.getValue());
                } else if (StringUtils.endsWith(entry.getKey(), "In")) {
                    queryWrapper.inSql(entry.getKey().substring(0, entry.getKey().length() - 2), entry.getValue());
                } else if (StringUtils.endsWith(entry.getKey(), "NotIn")) {
                    queryWrapper.notInSql(entry.getKey().substring(0, entry.getKey().length() - 5), entry.getValue());
                } else {
                    queryWrapper.eq(entry.getKey(), convertValue(type, entry.getKey(), entry.getValue()));
                }
            }
        }

        return queryWrapper;
    }
}
