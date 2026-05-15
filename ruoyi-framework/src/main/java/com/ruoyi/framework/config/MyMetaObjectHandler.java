package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Mybatis 自动填充功能
 * Mybatis 起始版本 3.3.0(推荐使用)
 *
 * @author ruoyi
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动填充-insert");
        // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "createTime", Date.class, new Timestamp(System.currentTimeMillis()));
        this.strictInsertFill(metaObject, "operTime", Date.class, new Timestamp(System.currentTimeMillis()));
        this.strictInsertFill(metaObject, "loginTime", Date.class, new Timestamp(System.currentTimeMillis()));
        if (StringUtils.isNotNull(SecurityUtils.getAuthentication())) {
            this.strictInsertFill(metaObject, "createBy", String.class, SecurityUtils.getUsername());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动填充-update");
        // 起始版本 3.3.0(推荐)
//        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Timestamp(System.currentTimeMillis()));
//        if (StringUtils.isNotNull(SecurityUtils.getAuthentication())) {
//            this.strictUpdateFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
//        }
    }

    /**
     * 严格模式填充策略,默认有值不覆盖,如果提供的值为null也不填充
     * modify-author: fsd
     * modify-issue: 重写strictFillStrategy方法，解决null值不自动填充问题
     *
     * @param metaObject metaObject meta object parameter
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value of Supplier
     * @return this
     * @since 3.3.0
     */
    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
        Object obj = fieldVal.get();
        if (Objects.nonNull(obj)) {
            metaObject.setValue(fieldName, obj);
        }
        return this;
    }
}

