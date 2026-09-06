package com.ruoyi.client.handler;

import com.ruoyi.client.enums.AVueAttrLevel;
import org.springframework.core.Ordered;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 根据级别处理数据
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/17 - 11:43
 */
public interface AVueLevelHandler extends Ordered {

    public boolean preHandler(AVueAttrLevel level, Annotation element);

    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
