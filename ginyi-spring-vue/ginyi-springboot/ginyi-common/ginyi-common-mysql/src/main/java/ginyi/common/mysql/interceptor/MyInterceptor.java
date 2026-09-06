package ginyi.common.mysql.interceptor;

import ginyi.common.annotation.CreateBy;
import ginyi.common.annotation.CreateTime;
import ginyi.common.annotation.UpdateBy;
import ginyi.common.annotation.UpdateTime;
import ginyi.common.constant.UserConstants;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Set;

/**
 * 自定义 Mybatis 插件，自动设置 createTime、createBy 和 updateTime、updateBy 的值。
 * 拦截 update 操作（添加和修改）
 */
@Component
@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
public class MyInterceptor implements Interceptor {

    @Resource
    private HttpServletRequest request;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        // 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        //只判断新增和修改
        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
            // 获取参数
            Object parameter = invocation.getArgs()[1];
            //批量操作时
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap map = (MapperMethod.ParamMap) parameter;
                Set keys = map.keySet();
                for (Object key : keys) {
                    if (map.get(key) != null) {
                        setParameter(map.get(key), sqlCommandType);
                    }
                }
            } else {
                setParameter(parameter, sqlCommandType);
            }
        }
        return invocation.proceed();
    }

    public void setParameter(Object parameter, SqlCommandType sqlCommandType) throws Throwable {
        Class<?> aClass = parameter.getClass();
        Field[] declaredFields = aClass.getSuperclass().getDeclaredFields();

        for (Field field : declaredFields) {
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                // insert 语句插入 createBy
                if (field.getAnnotation(CreateBy.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, request.getAttribute(UserConstants.CURRENT_USER));
                }

                // insert 语句插入 createTime
                if (field.getAnnotation(CreateTime.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, new Date());
                }
            }

            if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                // update 语句插入 updateBy
                if (field.getAnnotation(UpdateBy.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, request.getAttribute(UserConstants.CURRENT_USER));
                }
                // update 语句插入 updateTime
                if (field.getAnnotation(UpdateTime.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, new Date());
                }
            }
        }
    }
}