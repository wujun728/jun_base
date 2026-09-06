package com.geek.common.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.geek.common.utils.StringUtils;

/**
 * 通用的List类型处理器，可处理任何元素类型
 * 
 * 用法示例：
 * 1. 在XML中配置：
 * {@code <result column="tags" property="tags" 
 *          typeHandler=
"com.geek.generator.handler.GenericListTypeHandler$StringList"/>}
 * 
 * 2. 在字段上使用注解：
 * {@code @Result(column="tags", property="tags", 
 *          typeHandler=GenericListTypeHandler.StringList.class)}
 *
 * 3. 在xml插值语法中使用
 * {@code #{tags,typeHandler=com.geek.common.handler.GenericListTypeHandler$StringList}
 * }
 * 
 * @param <T> 列表元素类型
 */
public class GenericListTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private final Function<String, T> converter;

    /**
     * 构造函数
     * 
     * @param converter 字符串到元素类型T的转换器
     */
    protected GenericListTypeHandler(Function<String, T> converter) {
        this.converter = converter;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType)
            throws SQLException {
        String value = StringUtils.join(parameter, ",");
        ps.setString(i, value);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToList(rs.getString(columnName));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToList(rs.getString(columnIndex));
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToList(cs.getString(columnIndex));
    }

    private List<T> convertToList(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        List<T> list = new ArrayList<>();
        String[] items = value.split(",");
        for (String item : items) {
            if (StringUtils.isNotBlank(item)) {
                list.add(converter.apply(item.trim()));
            }
        }
        return list;
    }

    // 内部类 - 常用类型的TypeHandler实现

    /**
     * String类型列表的TypeHandler
     */
    public static class StringList extends GenericListTypeHandler<String> {
        public StringList() {
            super(String::valueOf);
        }
    }

    /**
     * Integer类型列表的TypeHandler
     */
    public static class IntegerList extends GenericListTypeHandler<Integer> {
        public IntegerList() {
            super(Integer::valueOf);
        }
    }

    /**
     * Long类型列表的TypeHandler
     */
    public static class LongList extends GenericListTypeHandler<Long> {
        public LongList() {
            super(Long::valueOf);
        }
    }

    /**
     * Double类型列表的TypeHandler
     */
    public static class DoubleList extends GenericListTypeHandler<Double> {
        public DoubleList() {
            super(Double::valueOf);
        }
    }

    /**
     * Boolean类型列表的TypeHandler
     */
    public static class BooleanList extends GenericListTypeHandler<Boolean> {
        public BooleanList() {
            super(Boolean::valueOf);
        }
    }
}
