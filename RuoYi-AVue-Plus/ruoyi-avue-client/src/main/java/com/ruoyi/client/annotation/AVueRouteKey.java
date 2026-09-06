package com.ruoyi.client.annotation;

import java.lang.annotation.*;

/**
 * 描述: 选项配置
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueRouteKey {

    String groupKey();

}
