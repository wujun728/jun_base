package com.danny.common.utils.sysdatapermissions;

import com.danny.common.utils.spring.SpringUtils;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * mybatis工具类
 */
public class MybatisUtil {

    /**
     * 根据mapperfile获得mapper名称
     */
    public static String getMapperName(String mapperFileName) {
        return mapperFileName.substring(mapperFileName.lastIndexOf("dao") + 4, mapperFileName.length() - 1);
    }

    /**
     * 根据mapper名称获得SQL
     */
    public static Map<String, String> getSqlByMapperName(String mapperName) {
        Map<String, String> sqlMap = new HashMap<String, String>(16);
        try {
            SqlSessionFactory sqlSessionFactory = SpringUtils.getBean(SqlSessionFactory.class);
            Collection<MappedStatement> mappedStatements = sqlSessionFactory.getConfiguration().getMappedStatements();

            for (Object mapped : mappedStatements) {
                if (mapped instanceof MappedStatement) {
                    MappedStatement mappedStatement = (MappedStatement) mapped;
                    SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
                    if ("SELECT".equals(sqlCommandType.toString())) {
                        String id = mappedStatement.getId();
                        String resource = mappedStatement.getResource();
                        String substring = resource.substring(0, 3);
                        int lastIndexOf = 0;
                        if ("URL".equals(substring)) {
                            lastIndexOf = resource.lastIndexOf("/");
                        } else {
                            lastIndexOf = resource.lastIndexOf("\\");
                        }
                        String str = resource.substring(lastIndexOf + 1);
                        resource = str.substring(0, str.length() - 1);
                        if(id.indexOf("selectJobLogAll") != -1){
                            System.out.println(id);
                        }
                        if (resource.equals(mapperName)) {
                            sqlMap.put(id.substring(id.lastIndexOf(".") + 1), mappedStatement.getBoundSql(null).getSql());
                        }
                    }
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlMap;
    }

    /**
     * 根据mapper名称获得方法
     */
    public static Set<String> getMethodByMapperName(String mapperName) {
        Map<String, Set<String>> allMapperMap = getAllMapperMap();
        Set<String> set = allMapperMap.get(mapperName);
        return set;
    }

    /**
     * 获得全部的mapper
     */
    public static Set<String> getAllMapperList() {
        Set<String> set = new HashSet<String>();
        try {
            //SqlSessionFactoryBean sqlSessionFactoryBean = SpringUtils.getBean(SqlSessionFactoryBean.class);
            //SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
            SqlSessionFactory sqlSessionFactory = SpringUtils.getBean(SqlSessionFactory.class);
            Collection<MappedStatement> mappedStatements = sqlSessionFactory.getConfiguration().getMappedStatements();

            for (Object mapped : mappedStatements) {
                if (mapped instanceof MappedStatement) {
                    MappedStatement mappedStatement = (MappedStatement) mapped;
                    String resource = mappedStatement.getResource();
                    String substring = resource.substring(0, 3);
                    int lastIndexOf = 0;
                    if ("URL".equals(substring)) {
                        lastIndexOf = resource.lastIndexOf("/");
                    } else if("fil".equals(substring)) {
                        lastIndexOf = resource.lastIndexOf("\\");
                    } else {
                        continue;
                    }
                    String str = resource.substring(lastIndexOf + 1);
                    resource = str.substring(0, str.length() - 1);

                    set.add(resource);
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    /**
     * 获得全部的mapper
     */
    public static Map<String, Set<String>> getAllMapperMap() {

        Map<String, Set<String>> mapperMap = new HashMap<String, Set<String>>(16);
        try {
            SqlSessionFactory sqlSessionFactory = SpringUtils.getBean(SqlSessionFactory.class);
            Collection<MappedStatement> mappedStatements = sqlSessionFactory.getConfiguration().getMappedStatements();

            for (Object mapped : mappedStatements) {
                if (mapped instanceof MappedStatement) {
                    MappedStatement mappedStatement = (MappedStatement) mapped;
                    SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
                    if ("SELECT".equals(sqlCommandType.toString())) {
                        String id = mappedStatement.getId();
                        String resource = mappedStatement.getResource();
                        String substring = resource.substring(0, 3);
                        int lastIndexOf = 0;
                        if ("URL".equals(substring)) {
                            lastIndexOf = resource.lastIndexOf("/");
                        } else {
                            lastIndexOf = resource.lastIndexOf("\\");
                        }
                        String str = resource.substring(lastIndexOf + 1);
                        resource = str.substring(0, str.length() - 1);

                        if (mapperMap.containsKey(resource)) {
                            mapperMap.get(resource.trim()).add(id);
                        } else {
                            Set<String> set = new HashSet<String>();
                            set.add(id);
                            mapperMap.put(resource.trim(), set);
                        }
                    }
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapperMap;
    }

    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId, String sql) {
        String showSql = showSql(configuration, boundSql, sql);
        StringBuffer sbf = new StringBuffer();
        sbf.append(showSql);
        return sbf.toString();
    }

    private static String showSql(Configuration configuration, BoundSql boundSql, String sql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
        sql = sql.replaceAll("[\\s]+", " ");
        if (!CollectionUtils.isEmpty(parameterMappingList) && parameterObject != null) {

            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);

                for (ParameterMapping parameterMapping : parameterMappingList) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    public static MappedStatement getNewMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if ((ms.getKeyProperties() != null) && (ms.getKeyProperties().length != 0)) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

}
