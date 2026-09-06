package com.ruoyi.common.handler;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.SmCryptoUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import javax.script.ScriptException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.VARCHAR)
public class SmCryptoTypeHandler<T> extends BaseTypeHandler<T> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, SmCryptoUtils.doSm4CbcEncrypt((String)parameter));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ALL")
    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        //有一些可能是空字符
        try {
            return StringUtils.isBlank(columnValue) ? (T)columnValue : (T) SmCryptoUtils.doSm4CbcDecrypt(columnValue);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ALL")
    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        try {
            return StringUtils.isBlank(columnValue) ? (T)columnValue : (T) SmCryptoUtils.doSm4CbcDecrypt(columnValue);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ALL")
    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        try {
            return StringUtils.isBlank(columnValue) ? (T)columnValue : (T) SmCryptoUtils.doSm4CbcDecrypt(columnValue);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
