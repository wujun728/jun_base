package com.geek.framework.mybatis;

import com.geek.common.core.domain.BaseEntity;
import com.geek.common.utils.DateUtils;
import com.geek.common.utils.SecurityUtils;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

public class BaseEntityListener implements InsertListener, UpdateListener {

    @Override
    public void onUpdate(Object arg0) {
        if (!(arg0 instanceof BaseEntity)) {
            return;
        }
        BaseEntity entity = (BaseEntity) arg0;
        entity.setUpdateTime(DateUtils.getNowDate());
        if (SecurityUtils.isAnonymous()) {
            entity.setUpdateBy(null);
        } else {
            entity.setUpdateBy(SecurityUtils.getUsername());
        }
    }

    @Override
    public void onInsert(Object arg0) {
        if (!(arg0 instanceof BaseEntity)) {
            return;
        }
        BaseEntity entity = (BaseEntity) arg0;
        entity.setCreateTime(DateUtils.getNowDate());
        if (SecurityUtils.isAnonymous()) {
            entity.setCreateBy(null);
        } else {
            entity.setCreateBy(SecurityUtils.getUsername());
        }
    }

}
