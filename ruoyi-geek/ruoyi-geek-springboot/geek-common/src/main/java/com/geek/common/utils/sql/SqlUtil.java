package com.geek.common.utils.sql;

import java.io.StringReader;
import java.util.Objects;

import com.geek.common.exception.UtilException;
import com.geek.common.utils.StringUtils;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.datasource.DataSourceKey;
import com.mybatisflex.core.datasource.FlexDataSource;
import com.mybatisflex.core.dialect.DbType;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

/**
 * sql操作工具类
 *
 * @author geek
 */
public class SqlUtil {

    private static final CCJSqlParserManager parserManager = new CCJSqlParserManager();

    /**
     * 定义常用的 sql关键字
     */
    public static String SQL_REGEX = "and |extractvalue|updatexml|sleep|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |union |like |+|/*|user()";

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            throw new UtilException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL关键字检查
     */
    public static void filterKeyword(String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords) {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1) {
                throw new UtilException("参数存在SQL注入风险");
            }
        }
    }

    public static Statement parseSql(String sql) throws JSQLParserException {
        return parserManager.parse(new StringReader(sql));
    }

    public static String findInSet(String str, String columns) {
        FlexDataSource flexDataSource = FlexGlobalConfig.getDefaultConfig().getDataSource();
        DbType dbType = Objects.requireNonNullElse(
                flexDataSource.getDbType(DataSourceKey.get()),
                flexDataSource.getDefaultDbType());
        if (dbType == DbType.OPENGAUSS) {
            return String.format("array_position(string_to_array(%s, ','), CAST(%s AS TEXT)) IS NOT NULL",
                    columns, str);
        } else if (dbType.postgresqlSameType()) {
            return String.format("CAST(%s AS TEXT) = ANY (string_to_array(%s, ','))", str, columns);
        } else if (dbType.oracleSameType()) {
            return String.format("INSTR(',' || %s || ',', ',' || %s || ',') > 0", columns, str);
        } else if (dbType.mysqlSameType()) {
            return "FIND_IN_SET(" + str + "," + columns + ")";
        } else {
            return "FIND_IN_SET(" + str + "," + columns + ")";
        }
    }

    private SqlUtil() {
    }
}
