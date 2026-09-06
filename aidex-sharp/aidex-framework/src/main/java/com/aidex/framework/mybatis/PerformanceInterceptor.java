package com.aidex.framework.mybatis;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Value;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * MyBatis 性能拦截器，用于输出每条 SQL 语句及其执行时间
 *
 * @author huangyong
 * @since 1.0.0
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class PerformanceInterceptor implements Interceptor {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long serialVersionUID = 1L;

    private Properties properties;
    @Value("${mybatis.isShowMyBatisSql}")
    private boolean  isShowMyBatisSql;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object returnValue = null;
        returnValue = invocation.proceed();
        // 通过配置文件判断是否打印SQL
        boolean showMyBatisSql = isShowMyBatisSql;
        try {
            if (true) {
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                Object parameter = null;
                if (invocation.getArgs().length > 1) {
                    parameter = invocation.getArgs()[1];
                }
                String sqlId = mappedStatement.getId();
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                Configuration configuration = mappedStatement.getConfiguration();
                long end = System.currentTimeMillis();
                long time = (end - start);
                if (time > 1) {
                    String sql = getSql(configuration, boundSql, sqlId, time);
                    System.err.println(sql);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage() + "================");
        }
        return returnValue;
    }


    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId);
        str.append(":");
        str.append(sql);
        str.append(":");
        str.append(time);
        str.append("ms");
        return str.toString();
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            value = "";
            Date date = new Date();
            if(null != obj){
                date = (Date)obj;
            }
            value = "'" + DATE_FORMAT.format(date) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    try {
                        if (metaObject.hasGetter(propertyName)) {
                            Object obj = metaObject.getValue(propertyName);
                            sql = sql.replaceFirst("\\?", getParameterValue(obj));
                        } else if (boundSql.hasAdditionalParameter(propertyName)) {
                            Object obj = boundSql.getAdditionalParameter(propertyName);
                            sql = sql.replaceFirst("\\?", getParameterValue(obj));
                        }
                    } catch (Exception e) {
                        System.out.println("propertyName:" + propertyName + ";ERROR");
                        e.printStackTrace();
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
