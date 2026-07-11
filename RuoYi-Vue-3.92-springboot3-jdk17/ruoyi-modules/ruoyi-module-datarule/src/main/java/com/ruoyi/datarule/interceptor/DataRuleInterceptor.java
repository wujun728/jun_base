package com.ruoyi.datarule.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.datarule.handler.DataRuleHandler;
import com.ruoyi.datarule.handler.DataRuleSqlHandler;
import com.ruoyi.datarule.model.DataRuleModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;


@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataRuleInterceptor implements Interceptor {


    private final DataRuleSqlHandler dataRuleSqlHandler;
    private final DataRuleHandler dataRuleHandler;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //未取到用户则放行
        LoginUser principal = SecurityUtils.getLoginUser();
        if (principal == null) {
            return invocation.proceed();
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        // this.sqlParser(metaObject); // MP 3.5+ removed AbstractSqlParserHandler

        //非SELECT操作放行
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()
                || StatementType.CALLABLE == mappedStatement.getStatementType()) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();

        //注解为空并且数据权限方法名未匹配到,则放行
        String mapperId = mappedStatement.getId();
        mapperId = mapperId.replace("_mpCount","");
        DataRuleModel dataScope = dataRuleHandler.getDataRule(mapperId, principal.getUser().getRoles());;
        //如果还不行，那就只有不处理了
        if (dataScope == null) {
            return invocation.proceed();
        }
        // 如果包含_mpCount，说明是分页，也需要过滤
        if (mappedStatement.getId().contains("_mpCount")){
            dataScope = BeanUtils.clone(dataScope);
            dataScope.setScopeField("COUNT(*)");
            originalSql = originalSql.replace("COUNT(*)", "*");
        }

        //获取数据权限规则对应的筛选Sql
        String sqlCondition = dataRuleSqlHandler.sqlCondition(dataScope, principal, originalSql);
        if (StringUtils.isBlank(sqlCondition)) {
            return invocation.proceed();
        } else {
            metaObject.setValue("delegate.boundSql.sql", sqlCondition);
            return invocation.proceed();
        }
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }


}
