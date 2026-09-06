package ginyi.common.mysql.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import ginyi.common.constant.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Resource
    private HttpServletRequest request;

    /**
     * 插入时的填充策略
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createBy", request.getAttribute(UserConstants.CURRENT_USER), metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", request.getAttribute(UserConstants.CURRENT_USER), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 更新时的填充策略
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateBy", request.getAttribute(UserConstants.CURRENT_USER), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}
