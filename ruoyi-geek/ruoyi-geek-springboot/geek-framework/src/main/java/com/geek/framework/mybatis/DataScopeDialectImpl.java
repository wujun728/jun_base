package com.geek.framework.mybatis;

import static com.mybatisflex.core.constant.SqlConsts.*;

import java.util.List;

import com.geek.common.utils.StringUtils;
import com.geek.framework.processor.context.DataScopeContextHolder;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.dialect.KeywordWrap;
import com.mybatisflex.core.dialect.LimitOffsetProcessor;
import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.dialect.impl.ClickhouseDialectImpl;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.dialect.impl.DB2105Dialect;
import com.mybatisflex.core.dialect.impl.DmDialect;
import com.mybatisflex.core.dialect.impl.OracleDialect;
import com.mybatisflex.core.dialect.impl.Sqlserver2005DialectImpl;
import com.mybatisflex.core.dialect.impl.SqlserverDialectImpl;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.table.TableInfo;

public class DataScopeDialectImpl {

    public static IDialect createDialect(DbType dbType) {
        switch (dbType) {
            case MYSQL:
            case H2:
            case MARIADB:
            case GBASE:
            case OSCAR:
            case XUGU:
            case OCEAN_BASE:
            case CUBRID:
            case GOLDILOCKS:
            case CSIIDB:
            case HIVE:
            case DORIS:
            case GOLDENDB:
            case SUNDB:
            case YASDB:
                return new DataScopeCommonsDialectImpl(KeywordWrap.BACK_QUOTE, LimitOffsetProcessor.MYSQL);
            case CLICK_HOUSE:
                return new DataScopeClickhouseDialectImpl(KeywordWrap.NONE, LimitOffsetProcessor.MYSQL);
            case GBASE_8S:
                return new DataScopeCommonsDialectImpl(KeywordWrap.NONE, LimitOffsetProcessor.MYSQL);
            case DM:
                return new DataScopeDmDialect();
            case ORACLE:
                return new DataScopeOracleDialect();
            case GAUSS:
                return new DataScopeCommonsDialectImpl(KeywordWrap.NONE, LimitOffsetProcessor.ORACLE);
            case POSTGRE_SQL:
            case SQLITE:
            case HSQL:
            case KINGBASE_ES:
            case PHOENIX:
            case SAP_HANA:
            case IMPALA:
            case HIGH_GO:
            case VERTICA:
            case REDSHIFT:
            case OPENGAUSS:
            case UXDB:
            case LEALONE:
            case DUCKDB:
            case GBASE_8C:
            case GBASE_8S_PG:
            case VASTBASE:
            case TRINO:
            case PRESTO:
                return new DataScopeCommonsDialectImpl(KeywordWrap.DOUBLE_QUOTATION, LimitOffsetProcessor.POSTGRESQL);
            case TDENGINE:
                return new DataScopeCommonsDialectImpl(KeywordWrap.BACK_QUOTE, LimitOffsetProcessor.POSTGRESQL);
            case ORACLE_12C:
                return new DataScopeOracleDialect(LimitOffsetProcessor.DERBY);
            case FIREBIRD:
            case DB2:
                return new DataScopeCommonsDialectImpl(KeywordWrap.NONE, LimitOffsetProcessor.DERBY);
            case DB2_1005:
                return new DataScopeDB2105Dialect(KeywordWrap.NONE, DB2105Dialect.DB2105LimitOffsetProcessor.DB2105);
            case SQLSERVER:
                return new DataScopeSqlserverDialectImpl(KeywordWrap.SQUARE_BRACKETS, LimitOffsetProcessor.SQLSERVER);
            case SQLSERVER_2005:
                return new DataScopeSqlserver2005DialectImpl(KeywordWrap.SQUARE_BRACKETS,
                        LimitOffsetProcessor.SQLSERVER_2005);
            case INFORMIX:
                return new DataScopeCommonsDialectImpl(KeywordWrap.NONE, LimitOffsetProcessor.INFORMIX);
            case SINODB:
                return new DataScopeCommonsDialectImpl(KeywordWrap.DOUBLE_QUOTATION, LimitOffsetProcessor.SINODB);
            case SYBASE:
                return new DataScopeCommonsDialectImpl(KeywordWrap.DOUBLE_QUOTATION, LimitOffsetProcessor.SYBASE);
            default:
                return new DataScopeCommonsDialectImpl();
        }
    }

    private static void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
        String dataScopeSql = DataScopeContextHolder.get();
        if (StringUtils.isEmpty(dataScopeSql)) {
            return;
        }
        List<QueryTable> queryTables = CPI.getQueryTables(queryWrapper);
        if (queryTables == null || queryTables.isEmpty()) {
            return;
        }
        QueryTable t = queryTables.get(0);
        if (queryTables.size() == 1 && "t".equals(t.getAlias()) && t.getName() == null && t.getSchema() == null) {
            QueryWrapper child = CPI.getChildSelect(queryWrapper).get(0);
            child.and(dataScopeSql);
        } else {
            queryWrapper.and(dataScopeSql);
        }
    }

    private static void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
        String dataScopeSql = DataScopeContextHolder.get();
        if (StringUtils.isNotEmpty(dataScopeSql)) {
            sql.append(AND).append(dataScopeSql);
        }
    }

    private static void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
        String dataScopeSql = DataScopeContextHolder.get();
        if (StringUtils.isNotEmpty(dataScopeSql)) {
            sql.append(AND).append(dataScopeSql);
        }
    }

    public static class DataScopeCommonsDialectImpl extends CommonsDialectImpl {
        public DataScopeCommonsDialectImpl() {
            super();
        }

        public DataScopeCommonsDialectImpl(LimitOffsetProcessor limitOffsetProcessor) {
            super(limitOffsetProcessor);
        }

        public DataScopeCommonsDialectImpl(KeywordWrap keywordWrap, LimitOffsetProcessor limitOffsetProcessor) {
            super(keywordWrap, limitOffsetProcessor);
        }

        @Override
        public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(queryWrapper, operateType);
            super.prepareAuth(queryWrapper, operateType);
        }

        @Override
        public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(schema, tableName, sql, operateType);
            super.prepareAuth(schema, tableName, sql, operateType);
        }

        @Override
        public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(tableInfo, sql, operateType);
            super.prepareAuth(tableInfo, sql, operateType);
        }
    }

    public static class DataScopeClickhouseDialectImpl extends ClickhouseDialectImpl {

        public DataScopeClickhouseDialectImpl(KeywordWrap keywordWrap, LimitOffsetProcessor limitOffsetProcessor) {
            super(keywordWrap, limitOffsetProcessor);
        }

        @Override
        public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(queryWrapper, operateType);
            super.prepareAuth(queryWrapper, operateType);
        }

        @Override
        public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(schema, tableName, sql, operateType);
            super.prepareAuth(schema, tableName, sql, operateType);
        }

        @Override
        public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(tableInfo, sql, operateType);
            super.prepareAuth(tableInfo, sql, operateType);
        }
    }

    public static class DataScopeDmDialect extends DmDialect {
        @Override
        public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(queryWrapper, operateType);
            super.prepareAuth(queryWrapper, operateType);
        }

        @Override
        public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(schema, tableName, sql, operateType);
            super.prepareAuth(schema, tableName, sql, operateType);
        }

        @Override
        public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(tableInfo, sql, operateType);
            super.prepareAuth(tableInfo, sql, operateType);
        }
    }

    public static class DataScopeOracleDialect extends OracleDialect {
        public DataScopeOracleDialect() {
            super();
        }

        public DataScopeOracleDialect(LimitOffsetProcessor limitOffsetProcessor) {
            super(limitOffsetProcessor);
        }

        public DataScopeOracleDialect(KeywordWrap keywordWrap, LimitOffsetProcessor limitOffsetProcessor) {
            super(keywordWrap, limitOffsetProcessor);
        }

        @Override
        public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(queryWrapper, operateType);
            super.prepareAuth(queryWrapper, operateType);
        }

        @Override
        public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(schema, tableName, sql, operateType);
            super.prepareAuth(schema, tableName, sql, operateType);
        }

        @Override
        public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(tableInfo, sql, operateType);
            super.prepareAuth(tableInfo, sql, operateType);
        }
    }

    public static class DataScopeDB2105Dialect extends DB2105Dialect {

        public DataScopeDB2105Dialect(KeywordWrap keywordWrap, LimitOffsetProcessor limitOffsetProcessor) {
            super(keywordWrap, limitOffsetProcessor);
        }

        @Override
        public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(queryWrapper, operateType);
            super.prepareAuth(queryWrapper, operateType);
        }

        @Override
        public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(schema, tableName, sql, operateType);
            super.prepareAuth(schema, tableName, sql, operateType);
        }

        @Override
        public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(tableInfo, sql, operateType);
            super.prepareAuth(tableInfo, sql, operateType);
        }
    }

    public static class DataScopeSqlserverDialectImpl extends SqlserverDialectImpl {
        public DataScopeSqlserverDialectImpl() {
            super();
        }

        public DataScopeSqlserverDialectImpl(LimitOffsetProcessor limitOffsetProcessor) {
            super(limitOffsetProcessor);
        }

        public DataScopeSqlserverDialectImpl(KeywordWrap keywordWrap, LimitOffsetProcessor limitOffsetProcessor) {
            super(keywordWrap, limitOffsetProcessor);
        }

        @Override
        public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(queryWrapper, operateType);
            super.prepareAuth(queryWrapper, operateType);
        }

        @Override
        public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(schema, tableName, sql, operateType);
            super.prepareAuth(schema, tableName, sql, operateType);
        }

        @Override
        public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(tableInfo, sql, operateType);
            super.prepareAuth(tableInfo, sql, operateType);
        }
    }

    public static class DataScopeSqlserver2005DialectImpl extends Sqlserver2005DialectImpl {
        public DataScopeSqlserver2005DialectImpl() {
            super();
        }

        public DataScopeSqlserver2005DialectImpl(LimitOffsetProcessor limitOffsetProcessor) {
            super(limitOffsetProcessor);
        }

        public DataScopeSqlserver2005DialectImpl(KeywordWrap keywordWrap, LimitOffsetProcessor limitOffsetProcessor) {
            super(keywordWrap, limitOffsetProcessor);
        }

        @Override
        public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(queryWrapper, operateType);
            super.prepareAuth(queryWrapper, operateType);
        }

        @Override
        public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(schema, tableName, sql, operateType);
            super.prepareAuth(schema, tableName, sql, operateType);
        }

        @Override
        public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
            DataScopeDialectImpl.prepareAuth(tableInfo, sql, operateType);
            super.prepareAuth(tableInfo, sql, operateType);
        }
    }
}